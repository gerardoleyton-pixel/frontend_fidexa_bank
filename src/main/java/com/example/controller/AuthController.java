package com.example.controller;

import com.example.dto.request.LoginRequestDTO;
import com.example.dto.response.UserDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.exception.business.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Optional<User> maybe = userRepository.findByEmail(request.getEmail());
        if (maybe.isEmpty()) {
            throw new ResourceNotFoundException("Usuario", "correo electrónico", request.getEmail());
        }

        User user = maybe.get();
        // Dev mode: compare plain text password (the project stores plain password currently)
        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        UserDTO dto = userMapper.toDTO(user);
        return ResponseEntity.ok(dto);
    }
}
