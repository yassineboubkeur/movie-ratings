package com.example.backend.service;

import com.example.backend.exception.DuplicateUserException;
import com.example.backend.exception.InvalidCredentialsException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.dto.UserLoginDTO;
import com.example.backend.model.dto.UserRegisterDTO;
import com.example.backend.model.entity.Role;
import com.example.backend.model.entity.User;
import com.example.backend.model.mapper.UserMapper;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
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

    @Autowired
    private JwtService jwtService;

    // 👉 Register user
    public UserDTO registerUser(UserRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateUserException("Username or email already exists");
        }

        Role role = roleRepository.findByName(dto.getRole().toUpperCase())
                .orElseThrow(() -> new UserNotFoundException("Role not found"));

        User user = userMapper.fromRegisterDTO(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // 👉 Login user (مدموج هنا)
    public Map<String, Object> login(UserLoginDTO dto) {
        Optional<User> userOpt = findByUsername(dto.getUsername());
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                java.util.List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))
        );

        String token = jwtService.generateToken(userDetails);
        UserDTO userDTO = toDTO(user);

        return Map.of("user", userDTO, "token", token);
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
