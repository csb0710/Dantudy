package com.study.board2.repository;

import com.study.board2.entity.Board;
import com.study.board2.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardOrderByIdDesc(Board board);

    List<Comment> findByCommentWriterContaining(String username, Pageable pageable);

}
