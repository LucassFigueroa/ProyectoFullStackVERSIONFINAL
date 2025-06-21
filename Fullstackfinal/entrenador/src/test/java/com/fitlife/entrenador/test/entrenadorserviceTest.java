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

    private entrenador entrenadorEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        entrenadorEntity = new entrenador(1L, "Santiago", "Musculación");
    }

    @Test
    void testCrearEntrenador() {
        when(entrenadorrepository.save(any(entrenador.class))).thenReturn(entrenadorEntity);

        entrenador result = entrenadorservice.crearEntrenador(entrenadorEntity);

        assertNotNull(result);
        assertEquals("Santiago", result.getNombre());
    }

    @Test
    void testObtenerEntrenadorPorId() {
        when(entrenadorrepository.findById(eq(1L))).thenReturn(Optional.of(entrenadorEntity));

        Optional<entrenador> result = entrenadorservice.obtenerEntrenadorPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("Santiago", result.get().getNombre());
    }
}
