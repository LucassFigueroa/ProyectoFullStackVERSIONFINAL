package com.fitlife.notificaciones.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fitlife.notificaciones.controller.notificacionescontroller;
import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.service.notificacionesservice;

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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(notificacionescontroller.class)
@AutoConfigureMockMvc(addFilters = false)
class notificacionescontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private notificacionesservice notificacionesService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private notificacionesmodel notificacion;

    @BeforeEach
    void setUp() {
        notificacion = notificacionesmodel.builder()
                .id(1L)
                .usuarioId(1L)
                .mensaje("Hola, tienes una nueva notificación.")
                .fechaEnvio(LocalDateTime.now())
                .estado("No Leida")
                .build();
    }

    @Test
    void testCrearNotificacion() throws Exception {
        notificacionesmodel nueva = notificacionesmodel.builder()
                .usuarioId(notificacion.getUsuarioId())
                .mensaje(notificacion.getMensaje())
                .fechaEnvio(notificacion.getFechaEnvio())
                .estado(notificacion.getEstado())
                .build();

        when(notificacionesService.crearNotificacion(any(notificacionesmodel.class))).thenReturn(notificacion);

        mockMvc.perform(post("/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testListarNotificaciones() throws Exception {
        when(notificacionesService.listarNotificaciones()).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testBuscarPorId() throws Exception {
        when(notificacionesService.buscarPorId(1L)).thenReturn(Optional.of(notificacion));

        mockMvc.perform(get("/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testActualizarNotificacion() throws Exception {
        when(notificacionesService.actualizarNotificacion(eq(1L), any(notificacionesmodel.class))).thenReturn(notificacion);

        mockMvc.perform(put("/notificaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testEliminarNotificacion() throws Exception {
        doNothing().when(notificacionesService).eliminarNotificacion(1L);

        mockMvc.perform(delete("/notificaciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testBuscarPorUsuarioId() throws Exception {
        when(notificacionesService.buscarPorUsuarioId(1L)).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificaciones/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1L));
    }

    @Test
    void testBuscarPorEstado() throws Exception {
        when(notificacionesService.buscarPorEstado("No Leida")).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificaciones/estado").param("estado", "No Leida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("No Leida"));
    }

    @Test
    void testBuscarPorUsuarioYEstado() throws Exception {
        when(notificacionesService.buscarPorUsuarioYEstado(1L, "No Leida")).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificaciones/usuarioEstado")
                        .param("usuarioId", "1")
                        .param("estado", "No Leida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1L))
                .andExpect(jsonPath("$[0].estado").value("No Leida"));
    }
}
