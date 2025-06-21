package com.fitlife.evaluacionfisica.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.evaluacionfisica.controller.evaluacionfisicacontroller;
import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.service.evaluacionfisicaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(evaluacionfisicacontroller.class)
public class evaluacionfisicacontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private evaluacionfisicaservice evaluacionfisicaservice;

    @Autowired
    private ObjectMapper objectMapper;

    private evaluacionfisica evaluacion;
    private Long id;
    private LocalDate fecha;

    @BeforeEach
    void setup() {
        id = 1L;
        fecha = LocalDate.of(2024, 6, 21);
        evaluacion = new evaluacionfisica(id, id, 70.0, 1.75, 22.86, fecha, "Todo bien");
    }

    @Test
    void testCrearEvaluacion() throws Exception {
        when(evaluacionfisicaservice.save(any(evaluacionfisica.class))).thenReturn(evaluacion);

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peso").value(70.0))
                .andExpect(jsonPath("$.observaciones").value("Todo bien"));
    }

    @Test
    void testGetAllEvaluaciones() throws Exception {
        when(evaluacionfisicaservice.getAll()).thenReturn(Arrays.asList(evaluacion));

        mockMvc.perform(get("/api/evaluaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].peso").value(70.0))
                .andExpect(jsonPath("$[0].observaciones").value("Todo bien"));
    }

    @Test
    void testGetById_OK() throws Exception {
        when(evaluacionfisicaservice.getById(id)).thenReturn(evaluacion);

        mockMvc.perform(get("/api/evaluaciones/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peso").value(70.0));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(evaluacionfisicaservice.getById(id)).thenReturn(null);

        mockMvc.perform(get("/api/evaluaciones/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_OK() throws Exception {
        when(evaluacionfisicaservice.update(Mockito.eq(id), any(evaluacionfisica.class))).thenReturn(evaluacion);

        mockMvc.perform(put("/api/evaluaciones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peso").value(70.0));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        when(evaluacionfisicaservice.update(Mockito.eq(id), any(evaluacionfisica.class))).thenReturn(null);

        mockMvc.perform(put("/api/evaluaciones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_OK() throws Exception {
        when(evaluacionfisicaservice.delete(id)).thenReturn(true);

        mockMvc.perform(delete("/api/evaluaciones/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(evaluacionfisicaservice.delete(id)).thenReturn(false);

        mockMvc.perform(delete("/api/evaluaciones/{id}", id))
                .andExpect(status().isNotFound());
    }
}
