package com.fitlife.reporte.test;

import com.fitlife.reporte.controller.reportecontroller;
import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.service.reporteservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(reportecontroller.class)
@AutoConfigureMockMvc(addFilters = false)  // Deshabilita seguridad para pruebas
public class reportecontrollertest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private reporteservice reporteservice;

    @Autowired
    private ObjectMapper objectMapper;

    private reportemodel reporte;

    @BeforeEach
    void setUp() {
        reporte = reportemodel.builder()
                .id(1L)
                .titulo("Reporte Test")
                .descripcion("Descripción test")
                .tipo("Asistencia")
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateReporte() throws Exception {
        when(reporteservice.save(any(reportemodel.class))).thenReturn(reporte);

        mockMvc.perform(post("/api/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reporte)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Reporte Test"));
    }

    @Test
    void testGetAllReportes() throws Exception {
        when(reporteservice.getAll()).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reportemodelList[0].id").value(1));
    }

    @Test
    void testGetReporteById_found() throws Exception {
        when(reporteservice.getById(1L)).thenReturn(reporte);

        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetReporteById_notFound() throws Exception {
        when(reporteservice.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/reportes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateReporte_found() throws Exception {
        reportemodel updated = reportemodel.builder()
                .id(1L)
                .titulo("Reporte Actualizado")
                .descripcion("Descripción actualizada")
                .tipo("Asistencia")
                .build();

        when(reporteservice.update(eq(1L), any(reportemodel.class))).thenReturn(updated);

        mockMvc.perform(put("/api/reportes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Reporte Actualizado"));
    }

    @Test
    void testUpdateReporte_notFound() throws Exception {
        when(reporteservice.update(eq(1L), any(reportemodel.class))).thenReturn(null);

        mockMvc.perform(put("/api/reportes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reporte)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteReporte_found() throws Exception {
        when(reporteservice.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteReporte_notFound() throws Exception {
        when(reporteservice.delete(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetByTipo() throws Exception {
        when(reporteservice.getByTipo("Asistencia")).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/reportes/filtro/tipo")
                .param("tipo", "Asistencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reportemodelList[0].tipo").value("Asistencia"));
    }

    @Test
    void testGetByFecha() throws Exception {
        String desde = LocalDateTime.now().minusDays(1).toString();
        String hasta = LocalDateTime.now().toString();

        when(reporteservice.getByFechaCreacionBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/reportes/filtro/fecha")
                .param("desde", desde)
                .param("hasta", hasta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reportemodelList[0].id").value(1));
    }
}
