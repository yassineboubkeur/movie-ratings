package com.example.backend.seeder;

import com.example.backend.model.entity.Role;
import com.example.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder().name("ADMIN").build();
            Role userRole = Role.builder().name("USER").build();

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            System.out.println("✅ Roles inserted into DB.");
        }
    }
}
