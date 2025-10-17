package com.example.controller;

import com.example.dto.request.UserCreateDTO;
import com.example.dto.response.UserDTO;
import com.example.exception.message.ErrorMessages;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 Pruebas unitarias para el controlador de usuarios.
 Verifica respuestas HTTP y validaciones en distintos escenarios.
 */
@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnUserById() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUsername("usuario1");
        dto.setEmail("usuario1@example.com");

        Mockito.when(userService.getUserById(1L)).thenReturn(dto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("usuario1"))
                .andExpect(jsonPath("$.email").value("usuario1@example.com"));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUsername("usuario1");

        Mockito.when(userService.getAllUsers()).thenReturn(java.util.List.of(dto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("usuario1"));
    }

    @Test
    void shouldReturn404WhenUserNotFoundByEmail() throws Exception {
        Mockito.when(userService.getUserByEmail("noexiste@example.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/users/by-email")
                        .param("email", "noexiste@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario con correo electrónico 'noexiste@example.com' no existe."));
    }

    @Test
    void shouldReturn404WhenUserNotFoundByUsername() throws Exception {
        Mockito.when(userService.getUserByUsername("usuarioDesconocido"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/users/by-username")
                        .param("username", "usuarioDesconocido"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario con nombre de usuario 'usuarioDesconocido' no existe."));
    }

    @Test
    void shouldReturn400WhenCreatingUserWithInvalidFields() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(""); // vacío
        dto.setEmail("correo-inválido");
        dto.setFullName(""); // vacío
        dto.setPassword(""); // vacío

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.username").value(
                        Matchers.anyOf(
                                Matchers.is(ErrorMessages.USERNAME_REQUIRED),
                                Matchers.is(ErrorMessages.USERNAME_LENGTH)
                        )
                ))
                .andExpect(jsonPath("$.details.fullName").value(
                        Matchers.anyOf(
                                Matchers.is(ErrorMessages.FULLNAME_REQUIRED),
                                Matchers.is(ErrorMessages.FULLNAME_LENGTH)
                        )
                ))
                .andExpect(jsonPath("$.details.email").value(ErrorMessages.EMAIL_FORMAT))
                .andExpect(jsonPath("$.details.password").value(
                        Matchers.anyOf(
                                Matchers.is(ErrorMessages.PASSWORD_REQUIRED),
                                Matchers.is(ErrorMessages.PASSWORD_LENGTH)
                        )
                ));
    }
}
