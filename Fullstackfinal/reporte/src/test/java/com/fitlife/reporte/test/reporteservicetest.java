package com.fitlife.reporte.test;

import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.repository.reporterepository;
import com.fitlife.reporte.service.reporteservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class reporteservicetest {

    @Mock
    private reporterepository reporterepository;

    @InjectMocks
    private reporteservice reporteservice;

    private reportemodel reporte;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        reporte = new reportemodel();
        reporte.setId(1L);
        reporte.setTitulo("Test");
        reporte.setDescripcion("Descripcion test");
        reporte.setTipo("test");
        reporte.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testSave() {
        when(reporterepository.save(any(reportemodel.class))).thenReturn(reporte);
        reportemodel result = reporteservice.save(reporte);
        assertNotNull(result);
        assertEquals("Test", result.getTitulo());
        verify(reporterepository).save(reporte);
    }

    @Test
    void testGetAll() {
        List<reportemodel> list = Collections.singletonList(reporte);
        when(reporterepository.findAll()).thenReturn(list);
        List<reportemodel> result = reporteservice.getAll();
        assertEquals(1, result.size());
        verify(reporterepository).findAll();
    }

    @Test
    void testGetById_found() {
        when(reporterepository.findById(1L)).thenReturn(Optional.of(reporte));
        reportemodel result = reporteservice.getById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reporterepository).findById(1L);
    }

    @Test
    void testGetById_notFound() {
        when(reporterepository.findById(1L)).thenReturn(Optional.empty());
        reportemodel result = reporteservice.getById(1L);
        assertNull(result);
        verify(reporterepository).findById(1L);
    }

    @Test
    void testGetByTipo() {
        List<reportemodel> list = Collections.singletonList(reporte);
        when(reporterepository.findByTipo("test")).thenReturn(list);
        List<reportemodel> result = reporteservice.getByTipo("test");
        assertEquals(1, result.size());
        verify(reporterepository).findByTipo("test");
    }

    @Test
    void testGetByFechaCreacionBetween() {
        LocalDateTime desde = LocalDateTime.now().minusDays(1);
        LocalDateTime hasta = LocalDateTime.now().plusDays(1);
        when(reporterepository.findByFechaCreacionBetween(desde, hasta)).thenReturn(List.of(reporte));

        List<reportemodel> result = reporteservice.getByFechaCreacionBetween(desde, hasta);

        assertEquals(1, result.size());
        verify(reporterepository).findByFechaCreacionBetween(desde, hasta);
    }

    @Test
    void testUpdate_found() {
        reportemodel updated = new reportemodel();
        updated.setTitulo("Nuevo título");
        updated.setDescripcion("Nueva descripción");
        updated.setTipo("nuevo");

        when(reporterepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporterepository.save(any(reportemodel.class))).thenReturn(reporte);

        reportemodel result = reporteservice.update(1L, updated);

        assertNotNull(result);
        assertEquals("Nuevo título", result.getTitulo());
        verify(reporterepository).findById(1L);
        verify(reporterepository).save(reporte);
    }

    @Test
    void testUpdate_notFound() {
        when(reporterepository.findById(1L)).thenReturn(Optional.empty());

        reportemodel result = reporteservice.update(1L, reporte);

        assertNull(result);
        verify(reporterepository).findById(1L);
        verify(reporterepository, never()).save(any());
    }

    @Test
    void testDelete_found() {
        when(reporterepository.existsById(1L)).thenReturn(true);
        boolean result = reporteservice.delete(1L);
        assertTrue(result);
        verify(reporterepository).existsById(1L);
        verify(reporterepository).deleteById(1L);
    }

    @Test
    void testDelete_notFound() {
        when(reporterepository.existsById(1L)).thenReturn(false);
        boolean result = reporteservice.delete(1L);
        assertFalse(result);
        verify(reporterepository).existsById(1L);
        verify(reporterepository, never()).deleteById(1L);
    }
}
