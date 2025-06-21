package com.fitlife.reporte.test;

import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.repository.reporterepository;
import com.fitlife.reporte.service.reporteservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

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
    }

    @Test
    void testGetById_found() {
        when(reporterepository.findById(1L)).thenReturn(Optional.of(reporte));
        reportemodel result = reporteservice.getById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetById_notFound() {
        when(reporterepository.findById(1L)).thenReturn(Optional.empty());
        reportemodel result = reporteservice.getById(1L);
        assertNull(result);
    }
}
