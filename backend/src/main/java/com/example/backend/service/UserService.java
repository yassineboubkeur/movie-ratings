package com.example.backend.service;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.dto.UserRegisterDTO;
import com.example.backend.model.entity.Role;
import com.example.backend.model.entity.User;
import com.example.backend.model.mapper.UserMapper;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO registerUser(UserRegisterDTO dto) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Username or email already exists");
        }

        // Get role from DB
        Role role = roleRepository.findByName(dto.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Map DTO to entity
        User user = userMapper.fromRegisterDTO(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        // Save user
        User savedUser = userRepository.save(user);

        // Return DTO
        return userMapper.toDTO(savedUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public UserDTO toDTO(User user) {
        return userMapper.toDTO(user);
    }
}