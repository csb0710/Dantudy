package com.study.board2.service;

import com.study.board2.entity.Role;
import com.study.board2.entity.User;
import com.study.board2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        Role role = new Role();

        role.setId(1L);

        user.getRoles().add(role);

        return userRepository.save(user);
    }

    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
