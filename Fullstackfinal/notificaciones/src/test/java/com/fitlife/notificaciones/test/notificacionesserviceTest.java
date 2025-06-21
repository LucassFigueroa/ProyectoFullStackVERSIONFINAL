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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class notificacionesserviceTest {

    @InjectMocks
    private notificacionesservice notificacionesService;

    @Mock
    private notificacionesrepository notificacionesRepository;

    private notificacionesmodel notificacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        notificacion = notificacionesmodel.builder()
                .id(1L)
                .usuarioId(1L)
                .mensaje("Hola, tienes una nueva notificación.")
                .fechaEnvio(LocalDateTime.now())
                .estado("No Leida")
                .build();
    }

    @Test
    void testCrearNotificacion() {
        when(notificacionesRepository.save(any(notificacionesmodel.class))).thenReturn(notificacion);

        notificacionesmodel resultado = notificacionesService.crearNotificacion(notificacion);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testListarNotificaciones() {
        when(notificacionesRepository.findAll()).thenReturn(List.of(notificacion));

        List<notificacionesmodel> lista = notificacionesService.listarNotificaciones();
        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorId() {
        when(notificacionesRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        Optional<notificacionesmodel> resultado = notificacionesService.buscarPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testActualizarNotificacion() {
        when(notificacionesRepository.existsById(1L)).thenReturn(true);
        when(notificacionesRepository.save(any(notificacionesmodel.class))).thenReturn(notificacion);

        notificacionesmodel resultado = notificacionesService.actualizarNotificacion(1L, notificacion);
        assertNotNull(resultado);
    }

    @Test
    void testActualizarNotificacion_NoExiste() {
        when(notificacionesRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                notificacionesService.actualizarNotificacion(99L, notificacion)
        );

        assertEquals("La notificacion con ID 99 no existe.", exception.getMessage());
    }

    @Test
    void testEliminarNotificacion() {
        when(notificacionesRepository.existsById(1L)).thenReturn(true);
        doNothing().when(notificacionesRepository).deleteById(1L);

        notificacionesService.eliminarNotificacion(1L);
        verify(notificacionesRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarNotificacion_NoExiste() {
        when(notificacionesRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                notificacionesService.eliminarNotificacion(99L)
        );

        assertEquals("La notificacion con ID 99 no existe.", exception.getMessage());
    }

    @Test
    void testBuscarPorUsuarioId() {
        when(notificacionesRepository.findByUsuarioId(1L)).thenReturn(List.of(notificacion));

        List<notificacionesmodel> lista = notificacionesService.buscarPorUsuarioId(1L);
        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorEstado() {
        when(notificacionesRepository.findByEstadoIgnoreCase("No Leida")).thenReturn(List.of(notificacion));

        List<notificacionesmodel> lista = notificacionesService.buscarPorEstado("No Leida");
        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorUsuarioYEstado() {
        when(notificacionesRepository.findByUsuarioIdAndEstadoIgnoreCase(1L, "No Leida"))
                .thenReturn(List.of(notificacion));

        List<notificacionesmodel> lista = notificacionesService.buscarPorUsuarioYEstado(1L, "No Leida");
        assertEquals(1, lista.size());
    }
}
