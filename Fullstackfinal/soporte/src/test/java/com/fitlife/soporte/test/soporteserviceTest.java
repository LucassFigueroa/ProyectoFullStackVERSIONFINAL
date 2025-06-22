package com.fitlife.soporte.test;

import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.repository.soporterepository;
import com.fitlife.soporte.service.soporteservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class soporteserviceTest {

    @Mock
    private soporterepository soporterepository;

    @InjectMocks
    private soporteservice soporteservice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        soportemodel soporte = new soportemodel(null, "Asunto", "Mensaje");
        soportemodel guardado = new soportemodel(1L, "Asunto", "Mensaje");

        when(soporterepository.save(soporte)).thenReturn(guardado);

        soportemodel result = soporteservice.save(soporte);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Asunto", result.getAsunto());
    }

    @Test
    void testGetAll() {
        List<soportemodel> lista = Arrays.asList(
                new soportemodel(1L, "A1", "M1"),
                new soportemodel(2L, "A2", "M2")
        );

        when(soporterepository.findAll()).thenReturn(lista);

        List<soportemodel> result = soporteservice.getAll();

        assertEquals(2, result.size());
        verify(soporterepository, times(1)).findAll();
    }

    @Test
    void testGetById_Encontrado() {
        soportemodel soporte = new soportemodel(1L, "Asunto", "Mensaje");

        when(soporterepository.findById(1L)).thenReturn(Optional.of(soporte));

        soportemodel result = soporteservice.getById(1L);

        assertNotNull(result);
        assertEquals("Asunto", result.getAsunto());
    }

    @Test
    void testGetById_NoEncontrado() {
        when(soporterepository.findById(99L)).thenReturn(Optional.empty());

        soportemodel result = soporteservice.getById(99L);

        assertNull(result);
    }

    @Test
    void testUpdate() {
        soportemodel existente = new soportemodel(1L, "Viejo", "Texto viejo");
        soportemodel nuevo = new soportemodel(null, "Nuevo", "Texto nuevo");
        soportemodel actualizado = new soportemodel(1L, "Nuevo", "Texto nuevo");

        when(soporterepository.findById(1L)).thenReturn(Optional.of(existente));
        when(soporterepository.save(any(soportemodel.class))).thenReturn(actualizado);

        soportemodel result = soporteservice.update(1L, nuevo);

        assertNotNull(result);
        assertEquals("Nuevo", result.getAsunto());
        assertEquals("Texto nuevo", result.getMensaje());
    }

    @Test
    void testDelete_Existente() {
        when(soporterepository.existsById(1L)).thenReturn(true);
        doNothing().when(soporterepository).deleteById(1L);

        boolean result = soporteservice.delete(1L);

        assertTrue(result);
        verify(soporterepository).deleteById(1L);
    }

    @Test
    void testDelete_NoExistente() {
        when(soporterepository.existsById(99L)).thenReturn(false);

        boolean result = soporteservice.delete(99L);

        assertFalse(result);
        verify(soporterepository, never()).deleteById(anyLong());
    }
}
