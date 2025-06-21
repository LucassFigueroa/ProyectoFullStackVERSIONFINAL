package com.fitlife.entrenador.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.entrenador.controller.entrenadorcontroller;
import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.service.entrenadorservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(entrenadorcontroller.class)
public class entrenadorcontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private entrenadorservice entrenadorservice;

    @Autowired
    private ObjectMapper objectMapper;

    private entrenador entrenadorEntity;

    @BeforeEach
    void setup() {
        entrenadorEntity = new entrenador(1L, "Santiago", "Musculación");
    }

    @Test
    void testCrearEntrenador() throws Exception {
        when(entrenadorservice.crearEntrenador(any(entrenador.class))).thenReturn(entrenadorEntity);

        mockMvc.perform(post("/api/entrenadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrenadorEntity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Santiago"))
                .andExpect(jsonPath("$.especialidad").value("Musculación"));
    }

    @Test
    void testObtenerEntrenadorPorId() throws Exception {
        when(entrenadorservice.obtenerEntrenadorPorId(eq(1L))).thenReturn(Optional.of(entrenadorEntity));

        mockMvc.perform(get("/api/entrenadores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Santiago"));
    }

   @Test
void testEliminarEntrenador() throws Exception {
    when(entrenadorservice.eliminarEntrenador(eq(1L))).thenReturn(true);

    mockMvc.perform(delete("/api/entrenadores/1"))
           .andExpect(status().isNoContent()); // 204
}
}