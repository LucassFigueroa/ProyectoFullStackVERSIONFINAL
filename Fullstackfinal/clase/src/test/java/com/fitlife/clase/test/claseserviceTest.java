package com.fitlife.clase.test;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.repository.claserepository;
import com.fitlife.clase.service.claseservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class claseserviceTest {

    @Mock
    private claserepository claserepository;

    @InjectMocks
    private claseservice claseservice;

    private clase claseEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        claseEntity = new clase(1L, "Yoga Power", "Sesión intensa", 60, 20, "Avanzado", 3L);
    }

    @Test
    void testGuardarClase() {
        when(claserepository.save(any(clase.class))).thenReturn(claseEntity);

        clase saved = claseservice.guardarClase(claseEntity);

        assertNotNull(saved);
        assertEquals("Yoga Power", saved.getNombreClase());
        verify(claserepository, times(1)).save(any(clase.class));
    }

    @Test
    void testObtenerClasePorId() {
        when(claserepository.findById(1L)).thenReturn(Optional.of(claseEntity));

        Optional<clase> found = claseservice.obtenerClasePorId(1L);

        assertTrue(found.isPresent());
        assertEquals("Yoga Power", found.get().getNombreClase());
    }

    @Test
    void testEliminarClase() {
        doNothing().when(claserepository).deleteById(1L);

        claseservice.eliminarClase(1L);

        verify(claserepository, times(1)).deleteById(1L);
    }
}
