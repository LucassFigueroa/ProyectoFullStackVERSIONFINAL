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
                .nombreArticulo("Máquina de Pecho Horizontal Profitness")
                .numeroSerie("PF-PH-2024")
                .cantidad(5)
                .estado("Funcional")
                .fechaIngreso(LocalDate.now())
                .build();
    }

    @Test
    void crearInventario_deberiaGuardarCorrectamente() {
        when(inventarioRepository.save(any(inventariomodel.class))).thenReturn(inventario);

        inventariomodel creado = inventarioService.crearInventario(inventario);

        assertNotNull(creado);
        assertEquals("Máquina de Pecho Horizontal Profitness", creado.getNombreArticulo());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void listarInventarios_deberiaRetornarLista() {
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        List<inventariomodel> lista = inventarioService.listarInventarios();

        assertEquals(1, lista.size());
        verify(inventarioRepository).findAll();
    }

    @Test
    void obtenerPorId_deberiaRetornarObjetoSiExiste() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        inventariomodel encontrado = inventarioService.obtenerPorId(1L);

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
    }

    @Test
    void actualizarInventario_deberiaActualizarSiExiste() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);
        when(inventarioRepository.save(any(inventariomodel.class))).thenReturn(inventario);

        inventariomodel actualizado = inventarioService.actualizarInventario(1L, inventario);

        assertNotNull(actualizado);
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void actualizarInventario_deberiaLanzarExcepcionSiNoExiste() {
        when(inventarioRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.actualizarInventario(1L, inventario);
        });

        assertTrue(exception.getMessage().contains("No existe el ID"));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void eliminarInventario_deberiaEjecutarDeleteById() {
        doNothing().when(inventarioRepository).deleteById(1L);

        inventarioService.eliminarInventario(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void buscarPorNombre_deberiaRetornarCoincidencias() {
        when(inventarioRepository.findByNombreArticuloContainingIgnoreCase("Pecho"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorNombre("Pecho");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorEstado_deberiaRetornarCoincidencias() {
        when(inventarioRepository.findByEstadoIgnoreCase("Funcional"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorEstado("Funcional");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorFecha_deberiaRetornarCoincidencias() {
        LocalDate fecha = LocalDate.now();
        when(inventarioRepository.findByFechaIngreso(fecha))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorFecha(fecha);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorRangoFecha_deberiaRetornarCoincidencias() {
        LocalDate desde = LocalDate.now().minusDays(1);
        LocalDate hasta = LocalDate.now().plusDays(1);

        when(inventarioRepository.findByFechaIngresoBetween(desde, hasta))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorRangoFecha(desde, hasta);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorNombreYEstado_deberiaRetornarCoincidencias() {
        when(inventarioRepository.findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase("Pecho", "Funcional"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultado = inventarioService.buscarPorNombreYEstado("Pecho", "Funcional");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorNumeroSerie_deberiaRetornarObjeto() {
        when(inventarioRepository.findByNumeroSerie("PF-PH-2024")).thenReturn(inventario);

        inventariomodel encontrado = inventarioService.buscarPorNumeroSerie("PF-PH-2024");

        assertNotNull(encontrado);
        assertEquals("PF-PH-2024", encontrado.getNumeroSerie());
    }

    @Test
    void buscarPorNumeroSerieParcial_deberiaRetornarLista() {
        when(inventarioRepository.findByNumeroSerieContainingIgnoreCase("PF-PH"))
                .thenReturn(List.of(inventario));

        List<inventariomodel> resultados = inventarioService.buscarPorNumeroSerieParcial("PF-PH");

        assertEquals(1, resultados.size());
    }
}
