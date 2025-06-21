package com.fitlife.pago.test;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.repository.pagorepository;
import com.fitlife.pago.service.pagoservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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

class pagoserviceTest {

    @InjectMocks
    private pagoservice pagoService;

    @Mock
    private pagorepository pagoRepository;

    private pagomodel pago;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pago = pagomodel.builder()
                .id(1L)
                .idCliente(1L)
                .monto(100.0)
                .metodoPago("Tarjeta")
                .fechaPago(LocalDate.now())
                .estado("Pendiente")
                .build();
    }

    @Test
    void testGuardarPago() {
        when(pagoRepository.save(any(pagomodel.class))).thenReturn(pago);

        pagomodel resultado = pagoService.guardarPago(pago);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    void testObtenerPagos() {
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        List<pagomodel> lista = pagoService.obtenerPagos();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Optional<pagomodel> resultado = pagoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(pagoRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarPago() {
        when(pagoRepository.existsById(1L)).thenReturn(true);
        when(pagoRepository.save(any(pagomodel.class))).thenReturn(pago);

        pagomodel resultado = pagoService.actualizarPago(1L, pago);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pagoRepository, times(1)).existsById(1L);
        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    void testEliminarPago() {
        when(pagoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(1L);

        pagoService.eliminarPago(1L);

        verify(pagoRepository, times(1)).existsById(1L);
        verify(pagoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testObtenerPagosPorCliente() {
        when(pagoRepository.findByIdCliente(1L)).thenReturn(List.of(pago));

        List<pagomodel> lista = pagoService.obtenerPagosPorCliente(1L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(pagoRepository, times(1)).findByIdCliente(1L);
    }

    @Test
    void testObtenerPagosPorEstado() {
        when(pagoRepository.findByEstado("Pendiente")).thenReturn(List.of(pago));

        List<pagomodel> lista = pagoService.obtenerPagosPorEstado("Pendiente");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(pagoRepository, times(1)).findByEstado("Pendiente");
    }

    @Test
    void testObtenerPagosPorClienteYEstado() {
        when(pagoRepository.findByIdClienteAndEstado(1L, "Pendiente")).thenReturn(List.of(pago));

        List<pagomodel> lista = pagoService.obtenerPagosPorClienteYEstado(1L, "Pendiente");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(pagoRepository, times(1)).findByIdClienteAndEstado(1L, "Pendiente");
    }

    @Test
    void testActualizarPago_NoExiste() {
        when(pagoRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                pagoService.actualizarPago(99L, pago)
        );

        assertEquals("Pago con ID 99 no existe", exception.getMessage());
        verify(pagoRepository, times(1)).existsById(99L);
    }

    @Test
    void testEliminarPago_NoExiste() {
        when(pagoRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                pagoService.eliminarPago(99L)
        );

        assertEquals("Pago con ID 99 no existe", exception.getMessage());
        verify(pagoRepository, times(1)).existsById(99L);
    }
}
