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
        Optional<User> existing = userRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new DuplicateEntityException("El correo '" + dto.getEmail() + "' ya está registrado.");
        }
        User user = userMapper.toEntity(dto);
        return userMapper.toDTO(userRepository.save(user));
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
                .orElseThrow(() ->
                        new ResourceNotFoundException("El usuario con ID " + id + " no existe. Por favor verificar."));
    }

    @Override
    public UserDTO updateUser(Long id, UserCreateDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró el usuario con ID " + id + " para actualizar."));

        existing.setUsername(dto.getUsername());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());
        existing.setFullName(dto.getFullName());

        return userMapper.toDTO(userRepository.save(existing));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El usuario con ID " + id + " no existe.");
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
