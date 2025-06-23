package com.fitlife.entrenador.test;

import com.fitlife.entrenador.controller.entrenadorcontroller;
import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.service.entrenadorservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(entrenadorcontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class entrenadorcontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private entrenadorservice entrenadorservice;

    @Autowired
    private ObjectMapper objectMapper;

    private entrenador entrenador;

    @BeforeEach
    void setUp() {
        entrenador = new entrenador(1L, "Pedro Gómez", "Yoga", "Senior");
    }

    @Test
    void testCreateEntrenador() throws Exception {
        when(entrenadorservice.saveEntrenador(any(entrenador.class))).thenReturn(entrenador);

        mockMvc.perform(post("/api/entrenadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrenador)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro Gómez"));
    }

    @Test
    void testGetAllEntrenadores() throws Exception {
        when(entrenadorservice.getAllEntrenadores()).thenReturn(List.of(entrenador));

        mockMvc.perform(get("/api/entrenadores"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEntrenadorById() throws Exception {
        when(entrenadorservice.getEntrenadorById(1L)).thenReturn(entrenador);

        mockMvc.perform(get("/api/entrenadores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro Gómez"));
    }

    @Test
    void testUpdateEntrenador() throws Exception {
        when(entrenadorservice.updateEntrenador(anyLong(), any(entrenador.class))).thenReturn(entrenador);

        mockMvc.perform(put("/api/entrenadores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrenador)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro Gómez"));
    }

    @Test
    void testDeleteEntrenador() throws Exception {
        when(entrenadorservice.deleteEntrenador(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/entrenadores/1"))
                .andExpect(status().isNoContent());
    }
}
