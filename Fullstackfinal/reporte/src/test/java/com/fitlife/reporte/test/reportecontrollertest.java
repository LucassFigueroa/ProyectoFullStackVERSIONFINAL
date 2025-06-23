package com.fitlife.reporte.test;

import com.fitlife.reporte.controller.reportecontroller;
import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.service.reporteservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
        reporte.setTipo("Asistencia");
    }

    @Test
    void testGetById_found() {
        when(reporteservice.getById(id)).thenReturn(reporte);

        ResponseEntity<EntityModel<reportemodel>> response = reportecontroller.getById(id);
        EntityModel<reportemodel> entityModel = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(entityModel);
        assertNotNull(entityModel.getContent());
        assertEquals(id, entityModel.getContent().getId());
        verify(reporteservice).getById(id);
    }

    @Test
    void testGetById_notFound() {
        when(reporteservice.getById(999L)).thenReturn(null);

        ResponseEntity<EntityModel<reportemodel>> response = reportecontroller.getById(999L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(reporteservice).getById(999L);
    }

    @Test
    void testGetAll() {
        List<reportemodel> lista = Collections.singletonList(reporte);
        when(reporteservice.getAll()).thenReturn(lista);

        CollectionModel<EntityModel<reportemodel>> resultado = reportecontroller.getAll(null, null);

        assertNotNull(resultado);
        List<EntityModel<reportemodel>> content = resultado.getContent().stream().toList();
        assertEquals(1, content.size());
        assertEquals(id, content.get(0).getContent().getId());
        verify(reporteservice).getAll();
    }
}
