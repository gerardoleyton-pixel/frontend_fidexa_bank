package com.example.service;

import com.example.dto.request.UserCreateDTO;
import com.example.dto.response.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserCreateDTO dto);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserCreateDTO dto);
    void deleteUser(Long id);

    Optional<UserDTO> getUserByEmail(String email);
    Optional<UserDTO> getUserByUsername(String username);
}
