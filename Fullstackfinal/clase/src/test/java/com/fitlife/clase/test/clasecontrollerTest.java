package com.fitlife.clase.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.clase.controller.clasecontroller;
import com.fitlife.clase.model.clase;
import com.fitlife.clase.service.claseservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(clasecontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class clasecontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private claseservice claseservice;

    @Autowired
    private ObjectMapper objectMapper;

    private clase claseEntity;

    @BeforeEach
    void setUp() {
        claseEntity = new clase(1L, "Yoga Power", "Sesión intensa", 60, 20, "Avanzado", 3L);
    }

    @Test
    void testCrearClase() throws Exception {
        when(claseservice.guardarClase(any(clase.class))).thenReturn(claseEntity);

        mockMvc.perform(post("/api/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claseEntity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreClase").value("Yoga Power"));
    }

    @Test
    void testObtenerClasePorId() throws Exception {
        when(claseservice.obtenerClasePorId(1L)).thenReturn(Optional.of(claseEntity));

        mockMvc.perform(get("/api/clases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreClase").value("Yoga Power"));
    }

   @Test
void testEliminarClase() throws Exception {
    doNothing().when(claseservice).eliminarClase(eq(1L));

    mockMvc.perform(delete("/api/clases/1"))
            .andExpect(status().isNoContent()); // Esto espera 204
}
}