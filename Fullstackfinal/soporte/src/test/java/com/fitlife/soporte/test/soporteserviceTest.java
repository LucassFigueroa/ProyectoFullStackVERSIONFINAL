package com.fitlife.soporte.test;

import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.repository.soporterepository;
import com.fitlife.soporte.service.soporteservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class soporteserviceTest {

    @Mock
    private soporterepository soporterepository;

    @InjectMocks
    private soporteservice soporteservice;

    private soportemodel soporte;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        soporte = new soportemodel(1L, "Problema Login", "No puedo entrar a mi cuenta", "PENDIENTE", "Cliente123");
    }

    @Test
    void testSaveSoporte() {
        when(soporterepository.save(any(soportemodel.class))).thenReturn(soporte);

        soportemodel saved = soporteservice.saveSoporte(soporte);

        assertNotNull(saved);
        assertEquals("Problema Login", saved.getAsunto());
        verify(soporterepository, times(1)).save(any(soportemodel.class));
    }

    @Test
    void testGetAllSoporte() {
        when(soporterepository.findAll()).thenReturn(List.of(soporte));

        List<soportemodel> list = soporteservice.getAllSoporte();

        assertFalse(list.isEmpty());
        verify(soporterepository, times(1)).findAll();
    }

    @Test
    void testGetSoporteById() {
        when(soporterepository.findById(1L)).thenReturn(Optional.of(soporte));

        soportemodel found = soporteservice.getSoporteById(1L);

        assertNotNull(found);
        assertEquals("Cliente123", found.getCliente());
    }

    @Test
    void testUpdateSoporte() {
        when(soporterepository.findById(1L)).thenReturn(Optional.of(soporte));
        when(soporterepository.save(any(soportemodel.class))).thenReturn(soporte);

        soportemodel updated = soporteservice.updateSoporte(1L, soporte);

        assertNotNull(updated);
        verify(soporterepository).save(any(soportemodel.class));
    }

    @Test
    void testDeleteSoporte() {
        when(soporterepository.existsById(1L)).thenReturn(true);
        doNothing().when(soporterepository).deleteById(1L);

        boolean deleted = soporteservice.deleteSoporte(1L);

        assertTrue(deleted);
        verify(soporterepository).deleteById(1L);
    }
}
