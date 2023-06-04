package com.study.board2.repository;

import com.study.board2.entity.Board;
import com.study.board2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = { "boards" }) // fetch 타입 무시용, 여러 개의 쿼리 발생 방지용
    List<User> findAll();
    User findByUsername(String username);

    Page<User> findByUsernameContaining(String searchKeyword, Pageable pageable);

    boolean existsUserByUsername(String username);
}
