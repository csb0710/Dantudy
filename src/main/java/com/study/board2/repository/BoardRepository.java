package com.study.board2.repository;

import com.study.board2.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {

    Page<Board> findAll(Specification spec, Pageable pageable);
    Page<Board> findByTitleContainingAndType(String searchKeyword, Integer type, Pageable pageable);
}
