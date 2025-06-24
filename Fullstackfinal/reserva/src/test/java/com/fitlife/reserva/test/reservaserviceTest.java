package com.fitlife.reserva.test;

import com.fitlife.reserva.model.reserva;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class reservaserviceTest {

    @Mock
    private reservarepository reservarepository;

    @InjectMocks
    private reservaservice reservaservice;

    private reserva reservaTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservaTest = reserva.builder()
                .id(1L)
                .clienteNombre("Juan Pérez")
                .fecha(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(10, 0))
                .estado("Activa")
                .build();
    }

    @Test
    void testSaveReserva_OK() {
        when(reservarepository.existsByClienteNombreAndFechaAndHora(anyString(), any(), any())).thenReturn(false);
        when(reservarepository.save(any(reserva.class))).thenReturn(reservaTest);

        reserva result = reservaservice.saveReserva(reservaTest);

        assertNotNull(result);
        verify(reservarepository, times(1)).save(reservaTest);
    }

    @Test
    void testGetAllReservas() {
        when(reservarepository.findAll()).thenReturn(List.of(reservaTest));

        List<reserva> result = reservaservice.getAllReservas();

        assertEquals(1, result.size());
    }

    @Test
    void testGetReservaById_Existente() {
        when(reservarepository.findById(1L)).thenReturn(Optional.of(reservaTest));

        Optional<reserva> result = reservaservice.getReservaById(1L);

        assertTrue(result.isPresent());
        assertEquals("Juan Pérez", result.get().getClienteNombre());
    }

    @Test
    void testGetReservaById_NoExiste() {
        when(reservarepository.findById(1L)).thenReturn(Optional.empty());

        Optional<reserva> result = reservaservice.getReservaById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateReserva_OK() {
        when(reservarepository.findById(1L)).thenReturn(Optional.of(reservaTest));
        when(reservarepository.save(any())).thenReturn(reservaTest);

        reserva updated = reservaservice.updateReserva(1L, reservaTest);

        assertNotNull(updated);
        verify(reservarepository).save(reservaTest);
    }

    @Test
    void testUpdateReserva_NoExiste() {
        when(reservarepository.findById(1L)).thenReturn(Optional.empty());

        reserva updated = reservaservice.updateReserva(1L, reservaTest);

        assertNull(updated);
    }

    @Test
    void testDeleteReserva_OK() {
        when(reservarepository.existsById(1L)).thenReturn(true);

        boolean result = reservaservice.deleteReserva(1L);

        assertTrue(result);
        verify(reservarepository).deleteById(1L);
    }

    @Test
    void testDeleteReserva_NoExiste() {
        when(reservarepository.existsById(1L)).thenReturn(false);

        boolean result = reservaservice.deleteReserva(1L);

        assertFalse(result);
        verify(reservarepository, never()).deleteById(any());
    }

    @Test
    void testGetReservasByClienteNombre() {
        when(reservarepository.findByClienteNombre("Juan Pérez")).thenReturn(List.of(reservaTest));

        List<reserva> result = reservaservice.getReservasByClienteNombre("Juan Pérez");

        assertEquals(1, result.size());
    }
}
