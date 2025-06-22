package com.fitlife.reserva.test;

import com.fitlife.reserva.model.reservamodel;
import com.fitlife.reserva.repository.reservarepository;
import com.fitlife.reserva.service.reservaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class reservaserviceTest {

    @Mock
    private reservarepository reservarepository;

    @InjectMocks
    private reservaservice reservaservice;

    private reservamodel reserva;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reserva = new reservamodel(1L, 1L, 2L, LocalDate.now().plusDays(1), LocalTime.of(10, 0));
    }

    @Test
    void testSave_ReservaNueva_OK() {
        when(reservarepository.existsByUsuarioIdAndFechaAndHora(anyLong(), any(), any())).thenReturn(false);
        when(reservarepository.save(any(reservamodel.class))).thenReturn(reserva);

        reservamodel result = reservaservice.save(reserva);

        assertNotNull(result);
        verify(reservarepository, times(1)).save(reserva);
    }

    @Test
    void testSave_ReservaDuplicada_Error() {
        when(reservarepository.existsByUsuarioIdAndFechaAndHora(anyLong(), any(), any())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservaservice.save(reserva));

        assertEquals("Ya tienes una reserva en ese horario.", exception.getMessage());
        verify(reservarepository, never()).save(any());
    }

    @Test
    void testGetById_Existente() {
        when(reservarepository.findById(1L)).thenReturn(Optional.of(reserva));

        reservamodel result = reservaservice.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetById_NoExiste() {
        when(reservarepository.findById(1L)).thenReturn(Optional.empty());

        reservamodel result = reservaservice.getById(1L);

        assertNull(result);
    }

    @Test
    void testUpdate_OK() {
        when(reservarepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservarepository.save(any())).thenReturn(reserva);

        reservamodel updated = reservaservice.update(1L, reserva);

        assertNotNull(updated);
        verify(reservarepository).save(reserva);
    }

    @Test
    void testDelete_Existente() {
        when(reservarepository.existsById(1L)).thenReturn(true);

        boolean result = reservaservice.delete(1L);

        assertTrue(result);
        verify(reservarepository).deleteById(1L);
    }

    @Test
    void testDelete_NoExiste() {
        when(reservarepository.existsById(1L)).thenReturn(false);

        boolean result = reservaservice.delete(1L);

        assertFalse(result);
        verify(reservarepository, never()).deleteById(any());
    }

    @Test
    void testGetAll() {
        when(reservarepository.findAll()).thenReturn(List.of(reserva));

        List<reservamodel> result = reservaservice.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void testGetByUsuarioId() {
        when(reservarepository.findByUsuarioId(1L)).thenReturn(List.of(reserva));

        List<reservamodel> result = reservaservice.getByUsuarioId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByFecha() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        when(reservarepository.findByFecha(fecha)).thenReturn(List.of(reserva));

        List<reservamodel> result = reservaservice.getByFecha(fecha);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByFechaBetween() {
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(2);
        when(reservarepository.findByFechaBetween(desde, hasta)).thenReturn(List.of(reserva));

        List<reservamodel> result = reservaservice.getByFechaBetween(desde, hasta);

        assertEquals(1, result.size());
    }
}
