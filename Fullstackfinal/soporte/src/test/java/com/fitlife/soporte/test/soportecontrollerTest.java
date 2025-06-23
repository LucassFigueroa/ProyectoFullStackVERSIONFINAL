package com.fitlife.soporte.test;

import com.fitlife.soporte.controller.soportecontroller;
import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.service.soporteservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(soportecontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class soportecontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private soporteservice soporteservice;

    @Autowired
    private ObjectMapper objectMapper;

    private soportemodel soporte;

    @BeforeEach
    void setUp() {
        soporte = new soportemodel(1L, "Problema Login", "No puedo entrar", "PENDIENTE", "Cliente123");
    }

    @Test
    void testCreateSoporte() throws Exception {
        when(soporteservice.saveSoporte(any(soportemodel.class))).thenReturn(soporte);

        mockMvc.perform(post("/api/soporte")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(soporte)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asunto").value("Problema Login"));
    }

    @Test
    void testGetAllSoporte() throws Exception {
        when(soporteservice.getAllSoporte()).thenReturn(List.of(soporte));

        mockMvc.perform(get("/api/soporte"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSoporteById() throws Exception {
        when(soporteservice.getSoporteById(1L)).thenReturn(soporte);

        mockMvc.perform(get("/api/soporte/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateSoporte() throws Exception {
        when(soporteservice.updateSoporte(any(Long.class), any(soportemodel.class))).thenReturn(soporte);

        mockMvc.perform(put("/api/soporte/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(soporte)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteSoporte() throws Exception {
        when(soporteservice.deleteSoporte(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/soporte/1"))
                .andExpect(status().isNoContent());
    }
}
