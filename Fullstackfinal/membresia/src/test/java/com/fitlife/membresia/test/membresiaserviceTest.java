package com.fitlife.membresia.test;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.repository.membresiarepository;
import com.fitlife.membresia.service.membresiaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class membresiaserviceTest {

    @Mock
    private membresiarepository membresiaRepository;

    @InjectMocks
    private membresiaservice membresiaService;

    private membresiamodel membresia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void guardarMembresia_deberiaGuardarCorrectamente() {
        when(membresiaRepository.save(any(membresiamodel.class))).thenReturn(membresia);

        membresiamodel guardada = membresiaService.guardarMembresia(membresia);

        assertNotNull(guardada);
        assertEquals("Premium", guardada.getTipo());
        verify(membresiaRepository).save(membresia);
    }

    @Test
    void obtenerTodas_deberiaRetornarLista() {
        when(membresiaRepository.findAll()).thenReturn(List.of(membresia));

        List<membresiamodel> lista = membresiaService.obtenerTodas();

        assertEquals(1, lista.size());
        verify(membresiaRepository).findAll();
    }

    @Test
    void obtenerPorId_deberiaRetornarObjeto() {
        when(membresiaRepository.findById(1L)).thenReturn(Optional.of(membresia));

        Optional<membresiamodel> encontrado = membresiaService.obtenerPorId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals(1L, encontrado.get().getId());
    }

    @Test
    void actualizarMembresia_deberiaActualizarSiExiste() {
        when(membresiaRepository.existsById(1L)).thenReturn(true);
        when(membresiaRepository.save(any(membresiamodel.class))).thenReturn(membresia);

        membresiamodel actualizado = membresiaService.actualizarMembresia(1L, membresia);

        assertNotNull(actualizado);
        verify(membresiaRepository).save(membresia);
    }

    @Test
    void actualizarMembresia_deberiaLanzarExcepcionSiNoExiste() {
        when(membresiaRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            membresiaService.actualizarMembresia(1L, membresia);
        });

        verify(membresiaRepository, never()).save(any());
    }

    @Test
    void eliminarMembresia_deberiaEjecutarDeleteById() {
        when(membresiaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(membresiaRepository).deleteById(1L);

        membresiaService.eliminarMembresia(1L);

        verify(membresiaRepository).deleteById(1L);
    }

    @Test
    void buscarPorUsuario_deberiaRetornarLista() {
        when(membresiaRepository.findByUsuarioId(123L)).thenReturn(List.of(membresia));

        List<membresiamodel> lista = membresiaService.buscarPorUsuario(123L);

        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorEstado_deberiaRetornarLista() {
        when(membresiaRepository.findByEstadoIgnoreCase("Activa")).thenReturn(List.of(membresia));

        List<membresiamodel> lista = membresiaService.buscarPorEstado("Activa");

        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorUsuarioYEstado_deberiaRetornarLista() {
        when(membresiaRepository.findByUsuarioIdAndEstadoIgnoreCase(123L, "Activa")).thenReturn(List.of(membresia));

        List<membresiamodel> lista = membresiaService.buscarPorUsuarioYEstado(123L, "Activa");

        assertEquals(1, lista.size());
    }
}
