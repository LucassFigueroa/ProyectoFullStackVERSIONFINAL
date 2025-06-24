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
    void saveMembresia_deberiaGuardarCorrectamente() {
        when(membresiaRepository.save(any(membresiamodel.class))).thenReturn(membresia);

        membresiamodel guardada = membresiaService.saveMembresia(membresia);

        assertNotNull(guardada);
        assertEquals("Premium", guardada.getTipo());
        verify(membresiaRepository).save(membresia);
    }

    @Test
    void getAllMembresias_deberiaRetornarLista() {
        when(membresiaRepository.findAll()).thenReturn(List.of(membresia));

        List<membresiamodel> lista = membresiaService.getAllMembresias();

        assertEquals(1, lista.size());
        verify(membresiaRepository).findAll();
    }

    @Test
    void getMembresiaById_deberiaRetornarObjeto() {
        when(membresiaRepository.findById(1L)).thenReturn(Optional.of(membresia));

        membresiamodel encontrado = membresiaService.getMembresiaById(1L);

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
    }

    @Test
    void updateMembresia_deberiaActualizarSiExiste() {
        when(membresiaRepository.findById(1L)).thenReturn(Optional.of(membresia));
        when(membresiaRepository.save(any(membresiamodel.class))).thenReturn(membresia);

        membresiamodel actualizado = membresiaService.updateMembresia(1L, membresia);

        assertNotNull(actualizado);
        verify(membresiaRepository).save(any());
    }

    @Test
    void updateMembresia_noExiste_deberiaRetornarNull() {
        when(membresiaRepository.findById(1L)).thenReturn(Optional.empty());

        membresiamodel actualizado = membresiaService.updateMembresia(1L, membresia);

        assertNull(actualizado);
    }

    @Test
    void deleteMembresia_deberiaEjecutarDeleteById() {
        when(membresiaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(membresiaRepository).deleteById(1L);

        boolean deleted = membresiaService.deleteMembresia(1L);

        assertTrue(deleted);
        verify(membresiaRepository).deleteById(1L);
    }

    @Test
    void deleteMembresia_noExiste_deberiaRetornarFalse() {
        when(membresiaRepository.existsById(1L)).thenReturn(false);

        boolean deleted = membresiaService.deleteMembresia(1L);

        assertFalse(deleted);
        verify(membresiaRepository, never()).deleteById(any());
    }
}
