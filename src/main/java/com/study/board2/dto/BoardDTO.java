package com.study.board2.dto;

import com.study.board2.entity.Board;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Integer id;
    private String title;
    private String contents;

    private MultipartFile boardFile; // save.html -> Controller 파일 담는 용도
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    public BoardDTO(Integer id, String boardTitle) {
        this.id = id;
        this.title = boardTitle;
    }

    public static BoardDTO toBoardDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContents(board.getContent());
        if (board.getFileAttached() == 0) {
            boardDTO.setFileAttached(board.getFileAttached()); // 0
        } else {
            boardDTO.setFileAttached(board.getFileAttached()); // 1

            boardDTO.setOriginalFileName(board.getBoardFiles().get(0).getOriginalFileName());
            boardDTO.setStoredFileName(board.getBoardFiles().get(0).getStoredFileName());
        }

        return boardDTO;
    }
}
