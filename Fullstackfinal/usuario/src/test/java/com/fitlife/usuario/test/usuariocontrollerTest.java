package com.fitlife.usuario.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.usuario.controller.usuariocontroller;
import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.service.usuarioservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // ✅ Importa esto
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(usuariocontroller.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ Desactiva filtros de seguridad para MockMvc
public class usuariocontrollerTest {

    @Autowired
    private MockMvc mockMvc;
@SuppressWarnings("removal")
    @MockBean
    private usuarioservice usuarioservice;

    @Autowired
    private ObjectMapper objectMapper;

    private usuariomodel user;

    @BeforeEach
    void setup() {
        user = new usuariomodel(1L, "AdminMaster", "admin@fitlife.com", "admin123", "ADMIN");
    }

    @Test
    void testRegister() throws Exception {
        when(usuarioservice.register(any(usuariomodel.class))).thenReturn(user);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("AdminMaster"))
                .andExpect(jsonPath("$.email").value("admin@fitlife.com"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    void testLogin_OK() throws Exception {
        when(usuarioservice.login(eq("admin@fitlife.com"), eq("admin123"))).thenReturn(user);

        String body = """
                {
                  "email": "admin@fitlife.com",
                  "contrasena": "admin123"
                }
                """;

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login OK ✅"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    void testLogin_Fallo() throws Exception {
        when(usuarioservice.login(eq("admin@fitlife.com"), eq("wrongpass"))).thenReturn(null);

        String body = """
                {
                  "email": "admin@fitlife.com",
                  "contrasena": "wrongpass"
                }
                """;

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas ❌"));
    }
}
