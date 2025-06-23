package com.fitlife.evaluacionfisica.test;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.repository.evaluacionfisicarepository;
import com.fitlife.evaluacionfisica.service.evaluacionfisicaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class evaluacionfisicaserviceTest {

    @Mock
    private evaluacionfisicarepository evaluacionfisicarepository;

    @InjectMocks
    private evaluacionfisicaservice evaluacionfisicaservice;

    private evaluacionfisica evaluacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        evaluacion = evaluacionfisica.builder()
                .id(1L)
                .clienteNombre("Juan Pérez")
                .peso(70.5)
                .altura(1.75)
                .imc(23.0)
                .evaluador("Coach Pedro")
                .build();
    }

    @Test
    void testSaveEvaluacion() {
        when(evaluacionfisicarepository.save(any(evaluacionfisica.class))).thenReturn(evaluacion);

        evaluacionfisica saved = evaluacionfisicaservice.saveEvaluacion(evaluacion);

        assertNotNull(saved);
        assertEquals("Juan Pérez", saved.getClienteNombre());
        verify(evaluacionfisicarepository, times(1)).save(any(evaluacionfisica.class));
    }

    @Test
    void testGetEvaluacionById() {
        when(evaluacionfisicarepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        evaluacionfisica found = evaluacionfisicaservice.getEvaluacionById(1L);

        assertNotNull(found);
        assertEquals("Juan Pérez", found.getClienteNombre());
    }

    @Test
    void testDeleteEvaluacion() {
        when(evaluacionfisicarepository.existsById(1L)).thenReturn(true);
        doNothing().when(evaluacionfisicarepository).deleteById(1L);

        boolean deleted = evaluacionfisicaservice.deleteEvaluacion(1L);

        assertTrue(deleted);
        verify(evaluacionfisicarepository, times(1)).deleteById(1L);
    }
}
