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

import java.util.List;
import java.util.Optional;

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

    private clase clase;

    @BeforeEach
    void setup() {
        clase = new clase();
        clase.setId(1L);
        clase.setNombreClase("Yoga");
        clase.setDescripcion("Clase de yoga");
    }

    @Test
    void testGuardarClase() throws Exception {
        when(claseservice.guardarClase(any(clase.class))).thenReturn(clase);

        mockMvc.perform(post("/api/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clase)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreClase").value("Yoga"));
    }

    @Test
    void testObtenerTodasLasClases() throws Exception {
        when(claseservice.obtenerTodasLasClases()).thenReturn(List.of(clase));

        mockMvc.perform(get("/api/clases"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerClasePorId() throws Exception {
        when(claseservice.obtenerClasePorId(1L)).thenReturn(Optional.of(clase));

        mockMvc.perform(get("/api/clases/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarClase() throws Exception {
        when(claseservice.actualizarClase(any(Long.class), any(clase.class))).thenReturn(clase);

        mockMvc.perform(put("/api/clases/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clase)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarClase() throws Exception {
        mockMvc.perform(delete("/api/clases/1"))
                .andExpect(status().isNoContent());
    }
}
