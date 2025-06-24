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
import static org.mockito.ArgumentMatchers.any;
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
                .nombreArticulo("Artículo Test")
                .numeroSerie("SERIE123")
                .cantidad(5)
                .estado("Disponible")
                .fechaIngreso(LocalDate.now())
                .build();
    }

    @Test
    void crearInventario_OK() {
        when(inventarioRepository.save(any(inventariomodel.class))).thenReturn(inventario);

        inventariomodel creado = inventarioService.crearInventario(inventario);

        assertNotNull(creado);
        assertEquals("SERIE123", creado.getNumeroSerie());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void listarInventarios_OK() {
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.listarInventarios();

        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorId_OK() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        inventariomodel resultado = inventarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("SERIE123", resultado.getNumeroSerie());
    }

    @Test
    void actualizarInventario_OK() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(inventariomodel.class))).thenReturn(inventario);

        inventariomodel actualizado = inventarioService.actualizarInventario(1L, inventario);

        assertNotNull(actualizado);
    }

    @Test
void actualizarInventario_lanzaExcepcion_siNoExiste() {
    when(inventarioRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> {
        inventarioService.actualizarInventario(1L, inventario);
    });
}

    @Test
    void eliminarInventario_OK() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        doNothing().when(inventarioRepository).deleteById(1L);

        inventarioService.eliminarInventario(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void buscarPorNombre_OK() {
        when(inventarioRepository.findByNombreArticuloContainingIgnoreCase("Art")).thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorNombre("Art");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorEstado_OK() {
        when(inventarioRepository.findByEstadoIgnoreCase("Disponible")).thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorEstado("Disponible");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorFecha_OK() {
        LocalDate fecha = LocalDate.now();
        when(inventarioRepository.findByFechaIngreso(fecha)).thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorFecha(fecha);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorRangoFecha_OK() {
        LocalDate desde = LocalDate.now().minusDays(1);
        LocalDate hasta = LocalDate.now().plusDays(1);
        when(inventarioRepository.findByFechaIngresoBetween(desde, hasta)).thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorRangoFecha(desde, hasta);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorNombreYEstado_OK() {
        when(inventarioRepository.findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase("Art", "Disponible"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorNombreYEstado("Art", "Disponible");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorNumeroSerie_OK() {
        when(inventarioRepository.findByNumeroSerie("SERIE123")).thenReturn(Optional.of(inventario));

        inventariomodel resultado = inventarioService.buscarPorNumeroSerie("SERIE123");

        assertNotNull(resultado);
        assertEquals("SERIE123", resultado.getNumeroSerie());
    }

    @Test
    void buscarPorNumeroSerieParcial_OK() {
        when(inventarioRepository.findByNumeroSerieContainingIgnoreCase("SERIE"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorNumeroSerieParcial("SERIE");

        assertEquals(1, resultado.size());
    }
}
