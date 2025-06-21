package com.fitlife.evaluacionfisica.test;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.repository.evaluacionfisicarepository;
import com.fitlife.evaluacionfisica.service.evaluacionfisicaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class evaluacionfisicaserviceTest {

    @Mock
    private evaluacionfisicarepository repo;

    @InjectMocks
    private evaluacionfisicaservice service;

    private evaluacionfisica eval;
    private Long id;
    private LocalDate fecha;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        id = 1L;
        fecha = LocalDate.of(2024, 6, 21);
        eval = new evaluacionfisica(id, id, 70.0, 1.75, 22.86, fecha, "Bien");
    }

    @Test
    void testSave() {
        when(repo.save(any())).thenReturn(eval);

        evaluacionfisica result = service.save(eval);

        assertNotNull(result);
        assertEquals(70.0, result.getPeso());
        verify(repo).save(any());
    }

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(eval));

        List<evaluacionfisica> lista = service.getAll();

        assertEquals(1, lista.size());
        verify(repo).findAll();
    }

    @Test
    void testGetById() {
        when(repo.findById(id)).thenReturn(Optional.of(eval));

        evaluacionfisica result = service.getById(id);

        assertNotNull(result);
        assertEquals(70.0, result.getPeso());
        verify(repo).findById(id);
    }

    @Test
    void testUpdate() {
        evaluacionfisica updated = new evaluacionfisica(id, id, 72.0, 1.75, 23.51, fecha, "Mejorando");

        when(repo.findById(id)).thenReturn(Optional.of(eval));
        when(repo.save(any())).thenReturn(updated);

        evaluacionfisica result = service.update(id, updated);

        assertNotNull(result);
        assertEquals(72.0, result.getPeso());
        assertEquals("Mejorando", result.getObservaciones());
        verify(repo).findById(id);
        verify(repo).save(any());
    }

    @Test
    void testDelete() {
        when(repo.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        assertTrue(deleted);
        verify(repo).deleteById(id);
    }
}
