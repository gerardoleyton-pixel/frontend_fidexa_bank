package com.example.service.impl;

import com.example.dto.request.UserCreateDTO;
import com.example.dto.response.UserDTO;
import com.example.entity.User;
import com.example.exception.business.DuplicateEntityException;
import com.example.exception.business.ResourceNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 Implementación del servicio de usuarios.
 Maneja operaciones CRUD y consultas por email o username.
 Aplica validaciones de duplicación y existencia.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

   @Override
    public UserDTO createUser(UserCreateDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateEntityException("Usuario", "correo electrónico", dto.getEmail());
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new DuplicateEntityException("Usuario", "nombre de usuario", dto.getUsername());
        }

        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }


    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    @Override
    public UserDTO updateUser(Long id, UserCreateDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        existing.setUsername(dto.getUsername());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());
        existing.setFullName(dto.getFullName());

        User updated = userRepository.save(existing);
        return userMapper.toDTO(updated);
    }


    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        userRepository.deleteById(id);
    }


    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }


    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO);
    }
}
