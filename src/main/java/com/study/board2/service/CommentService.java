package com.study.board2.service;

import com.study.board2.dto.CommentDTO;
import com.study.board2.entity.Board;
import com.study.board2.entity.Comment;
import com.study.board2.entity.User;
import com.study.board2.repository.BoardRepository;
import com.study.board2.repository.CommentRepository;
import com.study.board2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.tokens.CommentToken;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final BoardRepository boardRepository;
    @Autowired
    private  final UserRepository userRepository;

    public Long save(CommentDTO commentDTO) {
        Optional<Board> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if(optionalBoardEntity.isPresent()) {
            Board board = optionalBoardEntity.get();
            User user = userRepository.findById(commentDTO.getUserId()).get();
            Comment comment = Comment.toSaveEntity(commentDTO, board, user);

            return commentRepository.save(comment).getId();
        }
        else {
            return null;
        }
    }

    @Transactional
    public List<CommentDTO> findAll(Integer boardId, Long userId) {
        Board board = boardRepository.findById(boardId).get();
        List<Comment> commentList = commentRepository.findAllByBoardOrderByIdDesc(board);

        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(comment, boardId, userId);
            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }

    public Integer removeComment(Long id, String username) {
        Comment target = commentRepository.findById(id).get();

        if(target.getCommentWriter().equals(username)) {
            commentRepository.deleteById(id);
        }

        return target.getBoard().getId();

    }

    public Integer removeCommentOnlyId(Long id) {
        Comment target = commentRepository.findById(id).get();

        commentRepository.deleteById(id);


        return target.getBoard().getId();

    }
}
