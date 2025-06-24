package com.fitlife.inventario.test;

import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.repository.inventariorepository;
import com.fitlife.inventario.service.inventarioservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class inventarioserviceTest {

    @Mock
    private inventariorepository inventarioRepository;

    @InjectMocks
    private inventarioservice inventarioService;

    private inventariomodel inventario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventario = inventariomodel.builder()
                .id(1L)
                .nombreArticulo("Máquina de Pecho")
                .numeroSerie("SERIE123")
                .cantidad(5)
                .estado("Funcional")
                .fechaIngreso(LocalDate.now())
                .build();
    }

    @Test
    void crearInventario() {
        when(inventarioRepository.save(any())).thenReturn(inventario);

        inventariomodel creado = inventarioService.crearInventario(inventario);

        assertNotNull(creado);
        assertEquals("Máquina de Pecho", creado.getNombreArticulo());
    }

    @Test
    void listarInventarios() {
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.listarInventarios();

        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorId() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        inventariomodel resultado = inventarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("SERIE123", resultado.getNumeroSerie());
    }

    @Test
    void actualizarInventario_existe() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);
        when(inventarioRepository.save(any())).thenReturn(inventario);

        inventariomodel actualizado = inventarioService.actualizarInventario(1L, inventario);

        assertNotNull(actualizado);
    }

    @Test
    void actualizarInventario_noExiste() {
        when(inventarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.actualizarInventario(1L, inventario);
        });
    }

    @Test
    void eliminarInventario() {
        doNothing().when(inventarioRepository).deleteById(1L);

        inventarioService.eliminarInventario(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void buscarPorNombre() {
        when(inventarioRepository.findByNombreArticuloContainingIgnoreCase("Pecho"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.buscarPorNombre("Pecho");

        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorEstado() {
        when(inventarioRepository.findByEstadoIgnoreCase("Funcional"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.buscarPorEstado("Funcional");

        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorFecha() {
        when(inventarioRepository.findByFechaIngreso(any())).thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.buscarPorFecha(LocalDate.now());

        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorRangoFecha() {
        when(inventarioRepository.findByFechaIngresoBetween(any(), any()))
                .thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.buscarPorRangoFecha(LocalDate.now(), LocalDate.now());

        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorNombreYEstado() {
        when(inventarioRepository.findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase("Pecho", "Funcional"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.buscarPorNombreYEstado("Pecho", "Funcional");

        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorNumeroSerie() {
        when(inventarioRepository.findByNumeroSerie("SERIE123")).thenReturn(inventario);

        inventariomodel encontrado = inventarioService.buscarPorNumeroSerie("SERIE123");

        assertNotNull(encontrado);
    }

    @Test
    void buscarPorNumeroSerieParcial() {
        when(inventarioRepository.findByNumeroSerieContainingIgnoreCase("SERIE"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.buscarPorNumeroSerieParcial("SERIE");

        assertEquals(1, lista.size());
    }
}
