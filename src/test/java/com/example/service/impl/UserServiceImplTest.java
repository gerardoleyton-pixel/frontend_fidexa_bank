package com.example.service.impl;

import com.example.dto.request.UserCreateDTO;
import com.example.dto.response.UserDTO;
import com.example.entity.User;
import com.example.exception.business.DuplicateEntityException;
import com.example.exception.business.ResourceNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
 Pruebas unitarias para UserServiceImpl.
 Cubre escenarios exitosos y fallidos de creación, consulta, actualización y eliminación de usuarios.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldReturnUserDTOWhenIdExists() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(new UserDTO());

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userMapper).toDTO(user);
    }


    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
        verify(userRepository).findById(99L);
    }


    @Test
    void shouldCreateUserSuccessfully() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@example.com");

        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);
    }


    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("duplicate@example.com");

        when(userRepository.findByEmail("duplicate@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(dto));
        verify(userRepository).findByEmail("duplicate@example.com");
    }


    @Test
    void shouldUpdateUserSuccessfully() {
        Long id = 1L;
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("newUser");

        User existingUser = new User();
        User updatedUser = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(id, dto);

        assertNotNull(result);
        verify(userRepository).findById(id);
        verify(userRepository).save(existingUser);
        verify(userMapper).toDTO(updatedUser);
    }


    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentUser() {
        Long id = 99L;
        UserCreateDTO dto = new UserCreateDTO();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(id, dto));
        verify(userRepository).findById(id);
    }


    @Test
    void shouldDeleteUserSuccessfully() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
    }


    @Test
    void shouldThrowExceptionWhenDeletingNonexistentUser() {
        Long id = 99L;

        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(id));
        verify(userRepository).existsById(id);
    }


    @Test
    void shouldReturnUserByEmailIfExists() {
        String email = "test@example.com";
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        verify(userRepository).findByEmail(email);
        verify(userMapper).toDTO(user);
    }


    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        String email = "missing@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserByEmail(email);

        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(email);
    }


    @Test
    void shouldReturnUserByUsernameIfExists() {
        String username = "gerardo";
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserByUsername(username);

        assertTrue(result.isPresent());
        verify(userRepository).findByUsername(username);
        verify(userMapper).toDTO(user);
    }


    @Test
    void shouldReturnEmptyWhenUsernameNotFound() {
        String username = "missingUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserByUsername(username);

        assertTrue(result.isEmpty());
        verify(userRepository).findByUsername(username);
    }
}
