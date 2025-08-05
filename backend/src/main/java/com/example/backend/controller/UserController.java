package com.example.backend.controller;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.dto.UserLoginDTO;
import com.example.backend.model.dto.UserRegisterDTO;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegisterDTO dto) {
        UserDTO userDTO = userService.registerUser(dto);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

}
