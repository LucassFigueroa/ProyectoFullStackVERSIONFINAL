package com.fitlife.usuario.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.usuario.controller.usuariocontroller;
import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.service.usuarioservice;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(usuariocontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class usuariocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private usuarioservice usuarioservice;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister() throws Exception {
        usuariomodel returnedUser = new usuariomodel();
        returnedUser.setId(1L);
        returnedUser.setNombre("AdminMaster");
        returnedUser.setEmail("admin@fitlife.com");
        returnedUser.setContrasena("admin123");
        returnedUser.setRol("ADMIN");

        when(usuarioservice.register(any(usuariomodel.class))).thenReturn(returnedUser);

        String body = """
            {
              "nombre": "AdminMaster",
              "email": "admin@fitlife.com",
              "contrasena": "admin123"
            }
            """;

        mockMvc.perform(post("/usuario") // 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("AdminMaster"))
                .andExpect(jsonPath("$.email").value("admin@fitlife.com"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    void testLogin_OK() throws Exception {
        usuariomodel loginUser = new usuariomodel();
        loginUser.setId(1L);
        loginUser.setNombre("AdminMaster");
        loginUser.setEmail("admin@fitlife.com");
        loginUser.setContrasena("admin123");
        loginUser.setRol("ADMIN");

        when(usuarioservice.login(eq("admin@fitlife.com"), eq("admin123"))).thenReturn(loginUser);

        String body = """
            {
              "email": "admin@fitlife.com",
              "contrasena": "admin123"
            }
            """;

        mockMvc.perform(post("/usuario/login") //
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

        mockMvc.perform(post("/usuario/login") // 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }
}
