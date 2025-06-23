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
import java.util.List;

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
        verify(claserepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerTodasLasClases() {
        when(claserepository.findAll()).thenReturn(List.of(claseEntity));

        List<clase> clases = claseservice.obtenerTodasLasClases();

        assertNotNull(clases);
        assertEquals(1, clases.size());
        assertEquals("Yoga Power", clases.get(0).getNombreClase());
        verify(claserepository, times(1)).findAll();
    }

    @Test
    void testActualizarClase() {
        when(claserepository.findById(1L)).thenReturn(Optional.of(claseEntity));
        when(claserepository.save(any(clase.class))).thenReturn(claseEntity);

        clase updated = new clase(null, "Yoga Avanzado", "Nueva descripción", 75, 25, "Pro", 4L);

        clase result = claseservice.actualizarClase(1L, updated);

        assertNotNull(result);
        assertEquals("Yoga Avanzado", result.getNombreClase());
        assertEquals("Nueva descripción", result.getDescripcion());
        verify(claserepository, times(1)).save(any(clase.class));
    }

    @Test
    void testEliminarClase() {
        doNothing().when(claserepository).deleteById(1L);

        claseservice.eliminarClase(1L);

        verify(claserepository, times(1)).deleteById(1L);
    }
}
