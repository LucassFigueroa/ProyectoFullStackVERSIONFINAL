package com.fitlife.reporte.test;

import com.fitlife.reporte.controller.reportecontroller;
import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.service.reporteservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class reportecontrollertest {

    @Mock
    private reporteservice reporteservice;

    @InjectMocks
    private reportecontroller reportecontroller;

    private reportemodel reporte;
    private Long id = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reporte = new reportemodel();
        reporte.setId(id);
        // Puedes agregar más atributos si los tiene tu modelo
    }

    @Test
    void testGetById_found() {
        when(reporteservice.getById(id)).thenReturn(reporte);

        ResponseEntity<reportemodel> response = reportecontroller.getById(id);
        reportemodel resultado = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(reporteservice).getById(id);
    }

    @Test
    void testGetById_notFound() {
        when(reporteservice.getById(999L)).thenReturn(null);

        ResponseEntity<reportemodel> response = reportecontroller.getById(999L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(reporteservice).getById(999L);
    }

    @Test
    void testGetAll() {
        List<reportemodel> lista = Collections.singletonList(reporte);
        when(reporteservice.getAll()).thenReturn(lista);

        List<reportemodel> resultado = reportecontroller.getAll(null, null);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(reporteservice).getAll();
    }
}
