package com.fitlife.reserva.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.reserva.controller.reservacontroller;
import com.fitlife.reserva.model.reservamodel;
import com.fitlife.reserva.service.reservaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(reservacontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class reservacontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private reservaservice reservaservice;

    @Autowired
    private ObjectMapper objectMapper;

    private reservamodel reserva;

    @BeforeEach
    void setup() {
        reserva = new reservamodel(1L, 1L, 2L, LocalDate.now().plusDays(1), LocalTime.of(10, 0));
    }

    @Test
    void testCrearReserva_OK() throws Exception {
        when(reservaservice.save(any(reservamodel.class))).thenReturn(reserva);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.claseId").value(2))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testCrearReserva_Duplicada() throws Exception {
        when(reservaservice.save(any(reservamodel.class)))
                .thenThrow(new IllegalArgumentException("Ya tienes una reserva en ese horario."));

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya tienes una reserva en ese horario."));
    }

    @Test
    void testObtenerReservaPorId_OK() throws Exception {
        when(reservaservice.getById(1L)).thenReturn(reserva);

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testObtenerReservaPorId_NoExiste() throws Exception {
        when(reservaservice.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/reservas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarReserva_OK() throws Exception {
        when(reservaservice.update(eq(1L), any(reservamodel.class))).thenReturn(reserva);

        mockMvc.perform(put("/api/reservas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testEliminarReserva_OK() throws Exception {
        when(reservaservice.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarReserva_NoExiste() throws Exception {
        when(reservaservice.delete(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarReservasPorUsuario() throws Exception {
        when(reservaservice.getByUsuarioId(1L)).thenReturn(List.of(reserva));

        mockMvc.perform(get("/api/reservas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservamodelList[0].usuarioId").value(1))
                .andExpect(jsonPath("$._embedded.reservamodelList[0]._links.self.href").exists());
    }

    @Test
    void testBuscarReservasPorFecha() throws Exception {
        String fecha = LocalDate.now().plusDays(1).toString();
        when(reservaservice.getByFecha(any())).thenReturn(List.of(reserva));

        mockMvc.perform(get("/api/reservas/fecha")
                        .param("fecha", fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservamodelList[0].fecha").value(fecha));
    }

    @Test
    void testBuscarReservasPorRango() throws Exception {
        String desde = LocalDate.now().toString();
        String hasta = LocalDate.now().plusDays(5).toString();
        when(reservaservice.getByFechaBetween(any(), any())).thenReturn(List.of(reserva));

        mockMvc.perform(get("/api/reservas/rango")
                        .param("desde", desde)
                        .param("hasta", hasta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservamodelList[0].usuarioId").value(1));
    }
}
