package com.study.board2.dto;

import com.study.board2.entity.Board;
import com.study.board2.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Integer id;
    private User user;
    private String title;
    private String content;
    private Integer hits;

    private Integer period;

    private Integer times;

    private Integer time;

    private Integer people;

    private LocalDateTime createAt;

    private Integer type;

    private Integer completed;

    public BoardDTO(Integer id, String boardTitle) {
        this.id = id;
        this.title = boardTitle;
    }

    public static BoardDTO toBoardDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());
        boardDTO.setHits(board.getHits());
        boardDTO.setCreateAt(board.getCreatedAt());
        boardDTO.setUser(board.getUser());
        boardDTO.setType(board.getType());
        boardDTO.setCreateAt(board.getCreatedAt());
        boardDTO.setCompleted(board.getCompleted());

        boardDTO.setPeriod(board.getPeriod());
        boardDTO.setTimes(board.getTimes());
        boardDTO.setTime(board.getTime());
        boardDTO.setPeople(board.getPeople());

        return boardDTO;
    }

    public void checkNull() {
        if(this.period == null) {
            this.period = 1;
        }
        if(this.times == null) {
            this.times = 1;
        }
        if(this.time == null) {
            this.time = 1;
        }
        if(this.people == null) {
            this.people = 1;
        }
    }
}
