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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // Paso 1: Datos de entrada
        User user = new User();
        user.setId(1L);

        // Paso 2: Comportamientos simulados
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(new UserDTO());

        // Paso 3: Llamar al método
        UserDTO result = userService.getUserById(1L);

        // Paso 4: Verificar resultados
        assertNotNull(result);

        // Paso 5: Verificar interacciones
        verify(userRepository).findById(1L);
        verify(userMapper).toDTO(user);
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
        verify(userRepository).findById(99L);
    }
}
