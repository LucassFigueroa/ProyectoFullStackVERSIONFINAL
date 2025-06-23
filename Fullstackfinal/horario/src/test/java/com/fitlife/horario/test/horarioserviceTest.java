package com.fitlife.horario.test;

import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.repository.horariorepository;
import com.fitlife.horario.service.horarioservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class horarioserviceTest {

    @Mock
    private horariorepository horarioRepository;

    @InjectMocks
    private horarioservice horarioService;

    private horariomodel horario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        horario = horariomodel.builder()
                .id(1L)
                .entrenadorId(10L)
                .fechaHoraInicio(LocalDateTime.now().plusDays(1))
                .fechaHoraFin(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();
    }

    @Test
    void crearHorario_deberiaGuardar() {
        when(horarioRepository.save(any(horariomodel.class))).thenReturn(horario);

        horariomodel guardado = horarioService.crearHorario(horario);

        assertNotNull(guardado);
        assertEquals(10L, guardado.getEntrenadorId());
        verify(horarioRepository).save(horario);
    }

    @Test
    void getAllHorarios_deberiaRetornarLista() {
        when(horarioRepository.findAll()).thenReturn(List.of(horario));

        List<horariomodel> lista = horarioService.getAllHorarios();

        assertEquals(1, lista.size());
        verify(horarioRepository).findAll();
    }

    @Test
    void getHorarioById_deberiaRetornarObjetoSiExiste() {
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(horario));

        Optional<horariomodel> encontrado = horarioService.getHorarioById(1L);

        assertTrue(encontrado.isPresent());
        assertEquals(1L, encontrado.get().getId());
    }

    @Test
    void actualizarHorario_deberiaActualizar() {
        when(horarioRepository.existsById(1L)).thenReturn(true);
        when(horarioRepository.save(any(horariomodel.class))).thenReturn(horario);

        horariomodel actualizado = horarioService.actualizarHorario(1L, horario);

        assertNotNull(actualizado);
        verify(horarioRepository).save(horario);
    }

    @Test
    void actualizarHorario_deberiaLanzarExcepcionSiNoExiste() {
        when(horarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            horarioService.actualizarHorario(1L, horario);
        });

        verify(horarioRepository, never()).save(any());
    }

    @Test
    void eliminarHorario_deberiaEjecutarDeleteById() {
        when(horarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(horarioRepository).deleteById(1L);

        horarioService.eliminarHorario(1L);

        verify(horarioRepository).deleteById(1L);
    }

    @Test
    void eliminarHorario_deberiaLanzarExcepcionSiNoExiste() {
        when(horarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            horarioService.eliminarHorario(1L);
        });

        verify(horarioRepository, never()).deleteById(any());
    }

    @Test
    void buscarPorEntrenadorId_deberiaRetornarLista() {
        when(horarioRepository.findByEntrenadorId(10L)).thenReturn(List.of(horario));

        List<horariomodel> resultado = horarioService.buscarPorEntrenadorId(10L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorRango_deberiaRetornarLista() {
        LocalDateTime desde = LocalDateTime.now().plusDays(1);
        LocalDateTime hasta = LocalDateTime.now().plusDays(2);

        when(horarioRepository.findByFechaHoraInicioBetween(desde, hasta)).thenReturn(List.of(horario));

        List<horariomodel> resultado = horarioService.buscarPorRango(desde, hasta);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorEntrenadorYRango_deberiaRetornarLista() {
        LocalDateTime desde = LocalDateTime.now().plusDays(1);
        LocalDateTime hasta = LocalDateTime.now().plusDays(2);

        when(horarioRepository.findByEntrenadorIdAndFechaHoraInicioBetween(10L, desde, hasta))
                .thenReturn(List.of(horario));

        List<horariomodel> resultado = horarioService.buscarPorEntrenadorYRango(10L, desde, hasta);

        assertEquals(1, resultado.size());
    }
}
