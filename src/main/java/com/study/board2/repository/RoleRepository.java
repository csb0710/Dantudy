package com.study.board2.repository;

import com.study.board2.entity.Comment;
import com.study.board2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
