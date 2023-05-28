package com.study.board2.runner;

import com.study.board2.entity.Role;
import com.study.board2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role temp = roleRepository.findById(1L).get();
        if(temp == null) {
            Role role = new Role();
            role.setId(1L);
            role.setName("USER_ROLE");

            Role role2 = new Role();
            role.setId(2L);
            role.setName("ROLE_ADMIN");

            roleRepository.save(role);
            roleRepository.save(role2);
        }
    }
}
