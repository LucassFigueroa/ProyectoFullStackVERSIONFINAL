package com.fitlife.soporte.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.soporte.controller.soportecontroller;
import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.service.soporteservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@WebMvcTest(soportecontroller.class)
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
        soporte = new soportemodel(1L, "Asunto de prueba", "Mensaje de prueba");
    }

    @Test
    void testCreate() throws Exception {
        when(soporteservice.save(any())).thenReturn(soporte);

        mockMvc.perform(post("/api/soportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soporte)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.asunto").value("Asunto de prueba"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists())
                .andExpect(jsonPath("$._links.buscar-por-asunto.href").exists());
    }

    @Test
    void testGetAll() throws Exception {
        when(soporteservice.getAll()).thenReturn(List.of(soporte));

        mockMvc.perform(get("/api/soportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.soportemodelList").isArray())
                .andExpect(jsonPath("$._embedded.soportemodelList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.soportemodelList[0].asunto").value("Asunto de prueba"))
                .andExpect(jsonPath("$._embedded.soportemodelList[0]._links.self.href").exists());
    }

    @Test
    void testGetById_Found() throws Exception {
        when(soporteservice.getById(1L)).thenReturn(soporte);

        mockMvc.perform(get("/api/soportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.asunto").value("Asunto de prueba"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(soporteservice.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/soportes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_Found() throws Exception {
        when(soporteservice.update(eq(1L), any())).thenReturn(soporte);

        mockMvc.perform(put("/api/soportes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soporte)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.asunto").value("Asunto de prueba"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        when(soporteservice.update(eq(1L), any())).thenReturn(null);

        mockMvc.perform(put("/api/soportes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soporte)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_Found() throws Exception {
        when(soporteservice.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/soportes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(soporteservice.delete(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/soportes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchByAsunto() throws Exception {
        when(soporteservice.findByAsunto("Asunto")).thenReturn(List.of(soporte));

        mockMvc.perform(get("/api/soportes/search")
                        .param("keyword", "Asunto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.soportemodelList").isArray())
                .andExpect(jsonPath("$._embedded.soportemodelList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.soportemodelList[0]._links.self.href").exists());
    }
}
