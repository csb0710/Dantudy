package com.study.board2.service;

import com.study.board2.dto.BoardDTO;
import com.study.board2.entity.Board;
import com.study.board2.entity.Study.StudyForm;
import com.study.board2.repository.BoardFileRepository;
import com.study.board2.repository.BoardRepository;
import com.study.board2.repository.UserRepository;
import com.study.board2.specification.BoardSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardFileRepository boardFileRepository;

    public Page<Board> boardList(Integer type, Pageable pageable) {
        Specification<Board> spec = (root, query, criteriaBuilder) -> null;

        spec = spec.and(BoardSpecification.equalType(type));
        return boardRepository.findAll(spec, pageable);
    }
    public void write2(BoardDTO boardDTO, String username) throws IOException {
//        if(boardDTO.getBoardFile().isEmpty()) {
            Board board = Board.toSaveEntity(boardDTO);

            board.setUser(userRepository.findByUsername(username));
            board.setCreatedAt(LocalDateTime.now());
            boardRepository.save(board);
//        }
//        else {
//            MultipartFile boardFile = boardDTO.getBoardFile();
//            String originalFileName = boardFile.getOriginalFilename();
//            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
//            String savePath = "/Users/csb0710/Pictures/" + storedFileName;
//
//            boardFile.transferTo(new File(savePath));
//            Board board = Board.toSaveFileEntity(boardDTO);
//            board.setUser(userRepository.findByUsername(username));
//            board.setCreatedAt(LocalDateTime.now());
//
//            Integer savedId = boardRepository.save(board).getId();
//            Board getBoard = boardRepository.findById(savedId).get();
//            System.out.println(savedId);
//            BoardFile newBoardFile = BoardFile.toBoardFile(getBoard, originalFileName, storedFileName);
//
//            boardFileRepository.save(newBoardFile);
//
//        }
    }

    public void update(BoardDTO boardDTO, String username) {
        Board board = Board.toUpdateEntity(boardDTO);
        board.setUser(userRepository.findByUsername(username));

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

    public Page<Board> coditionFind(StudyForm codingStudy, Integer type, String searchKeyword, Pageable pageable) {
        codingStudy.checkNull();

        Specification<Board> spec = (root, query, criteriaBuilder) -> null;

        spec = spec.and(BoardSpecification.equalType(type)).and(BoardSpecification.equalPeriod(codingStudy))
                .and(BoardSpecification.equalTimes(codingStudy)).and(BoardSpecification.equalTime(codingStudy))
                .and(BoardSpecification.equalPeople(codingStudy));

        if(searchKeyword != null) {
            spec = spec.and(BoardSpecification.containTitle(searchKeyword));
        }

        return boardRepository.findAll(spec, pageable);
    }
}
