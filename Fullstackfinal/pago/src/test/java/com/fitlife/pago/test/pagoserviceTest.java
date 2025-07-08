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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class pagoserviceTest {

    @Mock
    private pagorepository pagorepository;

    @InjectMocks
    private pagoservice pagoservice;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearPago() {
        pagomodel pago = new pagomodel(1L, 100L, 2000, "Transferencia", LocalDate.now(), "Pagado");
        when(pagorepository.save(pago)).thenReturn(pago);

        pagomodel resultado = pagoservice.crearPago(pago);

        assertEquals(pago, resultado);
    }

    @Test
    public void testListarPagos() {
        List<pagomodel> pagos = Arrays.asList(new pagomodel(), new pagomodel());
        when(pagorepository.findAll()).thenReturn(pagos);

        List<pagomodel> resultado = pagoservice.listarPagos();

        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerPorId() {
        pagomodel pago = new pagomodel();
        when(pagorepository.findById(1L)).thenReturn(Optional.of(pago));

        pagomodel resultado = pagoservice.obtenerPorId(1L);

        assertEquals(pago, resultado);
    }

    @Test
    public void testActualizarPago() {
        pagomodel pago = new pagomodel(1L, 100L, 3000, "Efectivo", LocalDate.now(), "Pendiente");
        when(pagorepository.existsById(1L)).thenReturn(true);
        when(pagorepository.save(pago)).thenReturn(pago);

        pagomodel resultado = pagoservice.actualizarPago(1L, pago);

        assertEquals(3000, resultado.getMonto());
        verify(pagorepository).save(pago);
    }

    @Test
    public void testEliminarPago() {
        when(pagorepository.existsById(1L)).thenReturn(true);
        doNothing().when(pagorepository).deleteById(1L);

        pagoservice.eliminarPago(1L);

        verify(pagorepository).deleteById(1L);
    }

    @Test
    public void testBuscarPorEstado() {
        List<pagomodel> lista = Arrays.asList(new pagomodel(), new pagomodel());
        when(pagorepository.findByEstadoIgnoreCase("Pagado")).thenReturn(lista);

        List<pagomodel> resultado = pagoservice.buscarPorEstado("Pagado");

        assertEquals(2, resultado.size());
    }

    @Test
    public void testBuscarPorRangoFecha() {
        LocalDate desde = LocalDate.of(2023, 1, 1);
        LocalDate hasta = LocalDate.of(2023, 12, 31);
        List<pagomodel> lista = Arrays.asList(new pagomodel());
        when(pagorepository.findByFechaPagoBetween(desde, hasta)).thenReturn(lista);

        List<pagomodel> resultado = pagoservice.buscarPorRangoFecha(desde, hasta);

        assertEquals(1, resultado.size());
    }

    @Test
    public void testBuscarPorMontoMayor() {
        List<pagomodel> lista = Arrays.asList(new pagomodel());
        when(pagorepository.findByMontoGreaterThan(1000)).thenReturn(lista);

        List<pagomodel> resultado = pagoservice.buscarPorMontoMayor(1000);

        assertEquals(1, resultado.size());
    }

    @Test
    public void testBuscarPorMontoMenor() {
        List<pagomodel> lista = Arrays.asList(new pagomodel());
        when(pagorepository.findByMontoLessThan(5000)).thenReturn(lista);

        List<pagomodel> resultado = pagoservice.buscarPorMontoMenor(5000);

        assertEquals(1, resultado.size());
    }

    @Test
    public void testBuscarPorMetodo() {
        List<pagomodel> lista = Arrays.asList(new pagomodel());
        when(pagorepository.findByMetodoPagoIgnoreCase("Tarjeta")).thenReturn(lista);

        List<pagomodel> resultado = pagoservice.buscarPorMetodo("Tarjeta");

        assertEquals(1, resultado.size());
    }
}
