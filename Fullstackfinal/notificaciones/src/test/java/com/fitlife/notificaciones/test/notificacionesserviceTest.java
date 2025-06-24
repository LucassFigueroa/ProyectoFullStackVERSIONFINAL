package com.fitlife.notificaciones.test;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.repository.notificacionesrepository;
import com.fitlife.notificaciones.service.notificacionesservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class notificacionesserviceTest {

    @Mock
    private notificacionesrepository notificacionesRepo;

    @InjectMocks
    private notificacionesservice notificacionesService;

    private notificacionesmodel notif;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notif = notificacionesmodel.builder()
                .id(1L)
                .usuarioId(100L)
                .mensaje("Prueba de mensaje")
                .fechaEnvio(LocalDateTime.now())
                .estado("No Leida")
                .build();
    }

    @Test
    void crearNotificacion_deberiaGuardar() {
        when(notificacionesRepo.save(any(notificacionesmodel.class))).thenReturn(notif);

        notificacionesmodel creado = notificacionesService.crearNotificacion(notif);

        assertNotNull(creado);
        assertEquals("Prueba de mensaje", creado.getMensaje());
        verify(notificacionesRepo).save(notif);
    }

    @Test
    void listarNotificaciones_deberiaRetornarLista() {
        when(notificacionesRepo.findAll()).thenReturn(List.of(notif));

        List<notificacionesmodel> lista = notificacionesService.listarNotificaciones();

        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorId_deberiaRetornarOptional() {
        when(notificacionesRepo.findById(1L)).thenReturn(Optional.of(notif));

        Optional<notificacionesmodel> result = notificacionesService.obtenerPorId(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void actualizarNotificacion_deberiaActualizar() {
        when(notificacionesRepo.existsById(1L)).thenReturn(true);
        when(notificacionesRepo.save(any(notificacionesmodel.class))).thenReturn(notif);

        notificacionesmodel actualizado = notificacionesService.actualizarNotificacion(1L, notif);

        assertNotNull(actualizado);
    }

    @Test
    void actualizarNotificacion_noExisteId_deberiaLanzarExcepcion() {
        when(notificacionesRepo.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> notificacionesService.actualizarNotificacion(1L, notif));
    }

    @Test
    void eliminarNotificacion_deberiaEliminar() {
        when(notificacionesRepo.existsById(1L)).thenReturn(true);
        doNothing().when(notificacionesRepo).deleteById(1L);

        notificacionesService.eliminarNotificacion(1L);

        verify(notificacionesRepo).deleteById(1L);
    }

    @Test
    void buscarPorUsuario_deberiaRetornarLista() {
        when(notificacionesRepo.findByUsuarioId(100L)).thenReturn(List.of(notif));

        List<notificacionesmodel> resultado = notificacionesService.buscarPorUsuario(100L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorEstado_deberiaRetornarLista() {
        when(notificacionesRepo.findByEstadoIgnoreCase("No Leida")).thenReturn(List.of(notif));

        List<notificacionesmodel> resultado = notificacionesService.buscarPorEstado("No Leida");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorUsuarioYEstado_deberiaRetornarLista() {
        when(notificacionesRepo.findByUsuarioIdAndEstadoIgnoreCase(100L, "No Leida"))
                .thenReturn(List.of(notif));

        List<notificacionesmodel> resultado = notificacionesService.buscarPorUsuarioYEstado(100L, "No Leida");

        assertEquals(1, resultado.size());
    }

}
