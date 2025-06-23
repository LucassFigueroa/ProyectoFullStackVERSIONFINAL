package com.fitlife.evaluacionfisica.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.evaluacionfisica.controller.evaluacionfisicacontroller;
import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.service.evaluacionfisicaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(evaluacionfisicacontroller.class)
@AutoConfigureMockMvc(addFilters = false) // Para ignorar seguridad
public class evaluacionfisicacontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private evaluacionfisicaservice evaluacionfisicaservice;

    @Autowired
    private ObjectMapper objectMapper;

    private evaluacionfisica evaluacion;

    @BeforeEach
    void setup() {
        evaluacion = new evaluacionfisica(
                1L,
                "Juan Perez",
                70.0,
                1.75,
                22.9,
                "Entrenador Pedro",
                LocalDate.now()
        );
    }

    @Test
    void testCreateEvaluacion() throws Exception {
        when(evaluacionfisicaservice.saveEvaluacion(any(evaluacionfisica.class))).thenReturn(evaluacion);

        mockMvc.perform(post("/api/evaluaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteNombre").value("Juan Perez"));
    }

    @Test
    void testGetAllEvaluaciones() throws Exception {
        when(evaluacionfisicaservice.getAllEvaluaciones()).thenReturn(List.of(evaluacion));

        mockMvc.perform(get("/api/evaluaciones"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEvaluacionById() throws Exception {
        when(evaluacionfisicaservice.getEvaluacionById(1L)).thenReturn(evaluacion);

        mockMvc.perform(get("/api/evaluaciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEvaluacion() throws Exception {
        when(evaluacionfisicaservice.updateEvaluacion(any(Long.class), any(evaluacionfisica.class)))
                .thenReturn(evaluacion);

        mockMvc.perform(put("/api/evaluaciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEvaluacion() throws Exception {
        when(evaluacionfisicaservice.deleteEvaluacion(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/evaluaciones/1"))
                .andExpect(status().isNoContent());
    }
}
