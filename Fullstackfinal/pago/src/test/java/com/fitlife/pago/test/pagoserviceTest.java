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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class pagoserviceTest {

    @Mock
    private pagorepository pagoRepository;

    @InjectMocks
    private pagoservice pagoService;

    private pagomodel pago;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pago = pagomodel.builder()
                .id(1L)
                .estado("Pagado")
                .metodoPago("Tarjeta")
                .monto(15000.0)
                .fechaPago(LocalDate.now())
                .build();
    }

    @Test
    void crearPago_ok() {
        when(pagoRepository.save(pago)).thenReturn(pago);
        assertNotNull(pagoService.crearPago(pago));
    }

    @Test
    void listarPagos_ok() {
        when(pagoRepository.findAll()).thenReturn(List.of(pago));
        assertEquals(1, pagoService.listarPagos().size());
    }

    @Test
    void obtenerPorId_ok() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        assertEquals("Pagado", pagoService.obtenerPorId(1L).getEstado());
    }

    @Test
    void actualizarPago_ok() {
        when(pagoRepository.existsById(1L)).thenReturn(true);
        when(pagoRepository.save(pago)).thenReturn(pago);
        assertEquals("Pagado", pagoService.actualizarPago(1L, pago).getEstado());
    }

    @Test
    void actualizarPago_noExiste() {
        when(pagoRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> pagoService.actualizarPago(1L, pago));
    }

    @Test
    void eliminarPago_ok() {
        when(pagoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(1L);
        pagoService.eliminarPago(1L);
        verify(pagoRepository).deleteById(1L);
    }

    @Test
    void eliminarPago_noExiste() {
        when(pagoRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> pagoService.eliminarPago(1L));
    }

    @Test
    void buscarPorEstado_ok() {
        when(pagoRepository.findByEstadoIgnoreCase("Pagado")).thenReturn(List.of(pago));
        assertEquals(1, pagoService.buscarPorEstado("Pagado").size());
    }

    @Test
    void buscarPorRangoFecha_ok() {
        LocalDate fecha = LocalDate.now();
        when(pagoRepository.findByFechaPagoBetween(fecha, fecha)).thenReturn(List.of(pago));
        assertEquals(1, pagoService.buscarPorRangoFecha(fecha, fecha).size());
    }

    @Test
    void buscarPorMetodo_ok() {
        when(pagoRepository.findByMetodoPagoIgnoreCase("Tarjeta")).thenReturn(List.of(pago));
        assertEquals(1, pagoService.buscarPorMetodo("Tarjeta").size());
    }

    @Test
    void buscarPorMontoMayor_ok() {
        when(pagoRepository.findByMontoGreaterThan(10000.0)).thenReturn(List.of(pago));
        assertEquals(1, pagoService.buscarPorMontoMayor(10000.0).size());
    }

    @Test
    void buscarPorMontoMenor_ok() {
        when(pagoRepository.findByMontoLessThan(20000.0)).thenReturn(List.of(pago));
        assertEquals(1, pagoService.buscarPorMontoMenor(20000.0).size());
    }
}
