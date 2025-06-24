package com.fitlife.notificaciones.test;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(notificacionescontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class notificacionescontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private notificacionesservice notificacionesService;

    @Autowired
    private ObjectMapper objectMapper;

    private notificacionesmodel notif;

    @BeforeEach
    void setUp() {
        notif = notificacionesmodel.builder()
                .id(1L)
                .usuarioId(100L)
                .mensaje("Prueba de mensaje")
                .fechaEnvio(LocalDateTime.now())
                .estado("No Leida")
                .build();
    }

    @Test
    void crearNotificacion_deberiaRetornarOk() throws Exception {
        when(notificacionesService.crearNotificacion(any(notificacionesmodel.class))).thenReturn(notif);

        mockMvc.perform(post("/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notif)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Prueba de mensaje"));
    }

    @Test
    void listarNotificaciones_deberiaRetornarLista() throws Exception {
        when(notificacionesService.listarNotificaciones()).thenReturn(List.of(notif));

        mockMvc.perform(get("/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.notificacionesmodelList[0].mensaje").value("Prueba de mensaje"));
    }

    @Test
    void obtenerPorId_deberiaRetornarObjeto() throws Exception {
        when(notificacionesService.obtenerPorId(1L)).thenReturn(Optional.of(notif));

        mockMvc.perform(get("/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Prueba de mensaje"));
    }

    @Test
    void actualizarNotificacion_deberiaRetornarOk() throws Exception {
        when(notificacionesService.actualizarNotificacion(eq(1L), any(notificacionesmodel.class)))
                .thenReturn(notif);

        mockMvc.perform(put("/notificaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notif)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Prueba de mensaje"));
    }

    @Test
    void eliminarNotificacion_deberiaRetornarNoContent() throws Exception {
        doNothing().when(notificacionesService).eliminarNotificacion(1L);

        mockMvc.perform(delete("/notificaciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByUsuario_deberiaRetornarLista() throws Exception {
        when(notificacionesService.buscarPorUsuario(100L)).thenReturn(List.of(notif));

        mockMvc.perform(get("/notificaciones/usuario/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(100L));
    }

    @Test
    void getByEstado_deberiaRetornarLista() throws Exception {
        when(notificacionesService.buscarPorEstado("No Leida")).thenReturn(List.of(notif));

        mockMvc.perform(get("/notificaciones/estado")
                        .param("estado", "No Leida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("No Leida"));
    }

    @Test
    void getByUsuarioAndEstado_deberiaRetornarLista() throws Exception {
        when(notificacionesService.buscarPorUsuarioYEstado(100L, "No Leida")).thenReturn(List.of(notif));

        mockMvc.perform(get("/notificaciones/usuario/100/estado")
                        .param("estado", "No Leida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(100L));
    }

}
