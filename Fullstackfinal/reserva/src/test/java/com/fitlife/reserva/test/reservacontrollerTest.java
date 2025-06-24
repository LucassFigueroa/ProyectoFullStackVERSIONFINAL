package com.fitlife.reserva.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.reserva.controller.reservacontroller;
import com.fitlife.reserva.model.reserva;
import com.fitlife.reserva.service.reservaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private reserva reservaTest;

    @BeforeEach
    void setup() {
        reservaTest = reserva.builder()
                .id(1L)
                .clienteNombre("Juan Pérez")
                .fecha(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(10, 0))
                .estado("Activa")
                .build();

        // Contexto de autenticación manual para simular ADMIN real
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "adminsupremo",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
        );
    }

    @Test
    void testCrearReserva_OK() throws Exception {
        when(reservaservice.saveReserva(any(reserva.class))).thenReturn(reservaTest);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testCrearReserva_Duplicada() throws Exception {
        when(reservaservice.saveReserva(any(reserva.class)))
                .thenThrow(new IllegalArgumentException("Ya tienes una reserva en ese horario."));

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaTest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya tienes una reserva en ese horario."));
    }

    @Test
    void testGetAllReservas() throws Exception {
        when(reservaservice.getAllReservas()).thenReturn(List.of(reservaTest));

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservaList[0].clienteNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$._embedded.reservaList[0]._links.self.href").exists());
    }

    @Test
    void testGetReservaById_OK() throws Exception {
        when(reservaservice.getReservaById(1L)).thenReturn(Optional.of(reservaTest));

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testGetReservaById_NoExiste() throws Exception {
        when(reservaservice.getReservaById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateReserva_OK() throws Exception {
        when(reservaservice.updateReserva(eq(1L), any(reserva.class))).thenReturn(reservaTest);

        mockMvc.perform(put("/api/reservas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testDeleteReserva_OK() throws Exception {
        when(reservaservice.deleteReserva(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteReserva_NoExiste() throws Exception {
        when(reservaservice.deleteReserva(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetReservasByClienteNombre() throws Exception {
        when(reservaservice.getReservasByClienteNombre("Juan Pérez")).thenReturn(List.of(reservaTest));

        mockMvc.perform(get("/api/reservas/cliente/Juan Pérez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservaList[0].clienteNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$._embedded.reservaList[0]._links.self.href").exists());
    }
}
