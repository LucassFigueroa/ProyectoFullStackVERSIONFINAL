package com.fitlife.entrenador.test;

import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.repository.entrenadorrepository;
import com.fitlife.entrenador.service.entrenadorservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class entrenadorserviceTest {

    @Mock
    private entrenadorrepository entrenadorrepository;

    @InjectMocks
    private entrenadorservice entrenadorservice;

    private entrenador entrenador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entrenador = new entrenador(1L, "Pedro Gómez", "Yoga", "Senior");
    }

    @Test
    void testSaveEntrenador() {
        when(entrenadorrepository.save(any(entrenador.class))).thenReturn(entrenador);

        entrenador saved = entrenadorservice.saveEntrenador(entrenador);

        assertNotNull(saved);
        assertEquals("Pedro Gómez", saved.getNombre());
        verify(entrenadorrepository, times(1)).save(any(entrenador.class));
    }

    @Test
    void testGetEntrenadorById() {
        when(entrenadorrepository.findById(1L)).thenReturn(Optional.of(entrenador));

        entrenador found = entrenadorservice.getEntrenadorById(1L);

        assertNotNull(found);
        assertEquals("Pedro Gómez", found.getNombre());
    }

    @Test
    void testDeleteEntrenador() {
        when(entrenadorrepository.existsById(1L)).thenReturn(true);
        doNothing().when(entrenadorrepository).deleteById(1L);

        boolean deleted = entrenadorservice.deleteEntrenador(1L);

        assertTrue(deleted);
        verify(entrenadorrepository, times(1)).deleteById(1L);
    }
}
