package com.fitlife.soporte.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.soporte.controller.soportecontroller;
import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.service.soporteservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class soportecontrollerTest {

    private MockMvc mockMvc;

    @Mock
    private soporteservice soporteservice;

    @InjectMocks
    private soportecontroller soportecontroller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(soportecontroller).build();
    }

    @Test
    void testCreate() throws Exception {
        soportemodel input = new soportemodel(null, "Asunto", "Mensaje");
        soportemodel saved = new soportemodel(1L, "Asunto", "Mensaje");

        when(soporteservice.save(any(soportemodel.class))).thenReturn(saved);

        mockMvc.perform(post("/api/soportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.asunto").value("Asunto"));

        verify(soporteservice, times(1)).save(any(soportemodel.class));
    }

    @Test
    void testGetAll() throws Exception {
        soportemodel s1 = new soportemodel(1L, "A1", "M1");
        soportemodel s2 = new soportemodel(2L, "A2", "M2");

        when(soporteservice.getAll()).thenReturn(Arrays.asList(s1, s2));

        mockMvc.perform(get("/api/soportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(soporteservice, times(1)).getAll();
    }

    @Test
    void testGetById_Found() throws Exception {
        soportemodel s = new soportemodel(1L, "Asunto", "Mensaje");

        when(soporteservice.getById(1L)).thenReturn(s);

        mockMvc.perform(get("/api/soportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asunto").value("Asunto"));

        verify(soporteservice, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(soporteservice.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/soportes/1"))
                .andExpect(status().isNotFound());

        verify(soporteservice, times(1)).getById(1L);
    }

    @Test
    void testUpdate_Found() throws Exception {
        soportemodel input = new soportemodel(null, "Nuevo", "Mensaje nuevo");
        soportemodel updated = new soportemodel(1L, "Nuevo", "Mensaje nuevo");

        when(soporteservice.update(eq(1L), any(soportemodel.class))).thenReturn(updated);

        mockMvc.perform(put("/api/soportes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asunto").value("Nuevo"));

        verify(soporteservice, times(1)).update(eq(1L), any(soportemodel.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        soportemodel input = new soportemodel(null, "Nuevo", "Mensaje nuevo");

        when(soporteservice.update(eq(1L), any(soportemodel.class))).thenReturn(null);

        mockMvc.perform(put("/api/soportes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());

        verify(soporteservice, times(1)).update(eq(1L), any(soportemodel.class));
    }

    @Test
    void testDelete_Found() throws Exception {
        when(soporteservice.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/soportes/1"))
                .andExpect(status().isNoContent());

        verify(soporteservice, times(1)).delete(1L);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(soporteservice.delete(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/soportes/1"))
                .andExpect(status().isNotFound());

        verify(soporteservice, times(1)).delete(1L);
    }
}
