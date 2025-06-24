package com.fitlife.membresia.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.membresia.controller.membresiacontroller;
import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.service.membresiaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@WebMvcTest(membresiacontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class membresiacontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private membresiaservice membresiaService;

    @Autowired
    private ObjectMapper objectMapper;

    private membresiamodel membresia;

    @BeforeEach
    void setUp() {
        membresia = membresiamodel.builder()
                .id(1L)
                .usuarioId(123L)
                .tipo("Premium")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusMonths(1))
                .estado("Activa")
                .build();
    }

    @Test
    void crearMembresia_deberiaRetornarOk() throws Exception {
        when(membresiaService.saveMembresia(any(membresiamodel.class))).thenReturn(membresia);

        mockMvc.perform(post("/api/membresias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membresia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuarioId").value(123L))
                .andExpect(jsonPath("$.tipo").value("Premium"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void listarMembresias_deberiaRetornarLista() throws Exception {
        when(membresiaService.getAllMembresias()).thenReturn(List.of(membresia));

        mockMvc.perform(get("/api/membresias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.membresiamodelList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.membresiamodelList[0]._links.self.href").exists());
    }

    @Test
    void obtenerPorId_deberiaRetornarObjeto() throws Exception {
        when(membresiaService.getMembresiaById(1L)).thenReturn(membresia);

        mockMvc.perform(get("/api/membresias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("Premium"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void obtenerPorId_noExiste_deberiaRetornarNotFound() throws Exception {
        when(membresiaService.getMembresiaById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/membresias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarMembresia_deberiaRetornarOk() throws Exception {
        membresiamodel actualizado = membresiamodel.builder()
                .usuarioId(123L)
                .tipo("Premium Plus")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusMonths(2))
                .estado("Activa")
                .build();

        when(membresiaService.updateMembresia(eq(1L), any(membresiamodel.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/membresias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Premium Plus"));
    }

    @Test
    void eliminarMembresia_deberiaRetornarNoContent() throws Exception {
        when(membresiaService.deleteMembresia(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/membresias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarMembresia_NoExiste_deberiaRetornarNotFound() throws Exception {
        when(membresiaService.deleteMembresia(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/membresias/1"))
                .andExpect(status().isNotFound());
    }
}
