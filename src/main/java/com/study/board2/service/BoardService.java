package com.study.board2.service;

import com.study.board2.dto.BoardDTO;
import com.study.board2.entity.Board;
import com.study.board2.entity.BoardFile;
import com.study.board2.entity.User;
import com.study.board2.repository.BoardFileRepository;
import com.study.board2.repository.BoardRepository;
import com.study.board2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardFileRepository boardFileRepository;

    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }
    public void write2(BoardDTO boardDTO, String username) throws IOException {
        if(boardDTO.getBoardFile().isEmpty()) {
            Board board = Board.toSaveEntity(boardDTO);
            board.setUser(userRepository.findByUsername(username));
            boardRepository.save(board);
        }
        else {
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFileName = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            String savePath = "/Users/csb0710/Pictures/" + storedFileName;

            boardFile.transferTo(new File(savePath));
            Board board = Board.toSaveFileEntity(boardDTO);
            board.setUser(userRepository.findByUsername(username));

            Integer savedId = boardRepository.save(board).getId();
            Board getBoard = boardRepository.findById(savedId).get();
            System.out.println(savedId);
            BoardFile newBoardFile = BoardFile.toBoardFile(getBoard, originalFileName, storedFileName);

            boardFileRepository.save(newBoardFile);

        }
    }

    public void update(BoardDTO boardDTO, String username) {
        Board board = Board.toUpdateEntity(boardDTO);
        board.setUser(userRepository.findByUsername(username));

        boardRepository.save(board);
    }
    public void write(Board board, MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        board.setFilename(fileName);

        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);
    }

    public void write(Board board) {

        boardRepository.save(board);
    }

    public void checkHits(Integer id) {
        Board board = boardRepository.findById(id).get();

        if(board.getHits() == null) {
            board.setHits(0);
        }

        Integer temp = board.getHits();
        board.setHits(temp+1);

        this.write(board);

        return ;
    }

    public void reduceHits(Integer id) {
        Board board = boardRepository.findById(id).get();

        if(board.getHits() == null) {
            board.setHits(0);
        }

        Integer temp = board.getHits();
        board.setHits(temp-1);

        this.write(board);

        return ;
    }

    public Board boardView(Integer id) {

        return boardRepository.findById(id).orElse(null);
    }

    public String deleteView(Integer id) {
        String username = boardRepository.findById(id).get().getUser().getUsername();

        boardRepository.deleteById(id);

        return username;
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}
