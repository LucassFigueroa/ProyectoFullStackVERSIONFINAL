package com.fitlife.inventario.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.inventario.controller.inventariocontroller;
import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.service.inventarioservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(inventariocontroller.class)
@AutoConfigureMockMvc(addFilters = false)
class inventariocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private inventarioservice inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private inventariomodel inventario;

    @BeforeEach
    void setUp() {
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
    void crearInventario() throws Exception {
        when(inventarioService.crearInventario(any())).thenReturn(inventario);

        mockMvc.perform(post("/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho"))
                .andExpect(jsonPath("$.numeroSerie").value("SERIE123"));
    }

    @Test
    void listarInventarios() throws Exception {
        when(inventarioService.listarInventarios()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.inventariomodelList[0].nombreArticulo").value("Máquina de Pecho"));
    }

    @Test
    void obtenerInventario() throws Exception {
        when(inventarioService.obtenerPorId(1L)).thenReturn(inventario);

        mockMvc.perform(get("/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho"));
    }

    @Test
    void obtenerInventario_IdInvalido() throws Exception {
        mockMvc.perform(get("/inventario/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El ID proporcionado no es válido"));
    }

    @Test
    void actualizarInventario() throws Exception {
        when(inventarioService.actualizarInventario(eq(1L), any())).thenReturn(inventario);

        mockMvc.perform(put("/inventario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho"));
    }

    @Test
    void eliminarInventario() throws Exception {
        doNothing().when(inventarioService).eliminarInventario(1L);

        mockMvc.perform(delete("/inventario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorNombre() throws Exception {
        when(inventarioService.buscarPorNombre("Pecho")).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarNombre").param("nombre", "Pecho"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho"));
    }

    @Test
    void buscarPorEstado() throws Exception {
        when(inventarioService.buscarPorEstado("Funcional")).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarEstado").param("estado", "Funcional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Funcional"));
    }

    @Test
    void buscarPorFecha() throws Exception {
        String fecha = LocalDate.now().toString();
        when(inventarioService.buscarPorFecha(LocalDate.parse(fecha)))
                .thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarFecha").param("fecha", fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho"));
    }

    @Test
    void buscarPorRango() throws Exception {
        String fecha = LocalDate.now().toString();
        when(inventarioService.buscarPorRangoFecha(LocalDate.parse(fecha), LocalDate.parse(fecha)))
                .thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarRango")
                .param("desde", fecha)
                .param("hasta", fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho"));
    }

    @Test
    void buscarPorNombreYEstado() throws Exception {
        when(inventarioService.buscarPorNombreYEstado("Pecho", "Funcional"))
                .thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarAvanzado")
                .param("nombre", "Pecho")
                .param("estado", "Funcional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Funcional"));
    }

    @Test
    void buscarPorSerieExacta() throws Exception {
        when(inventarioService.buscarPorNumeroSerie("SERIE123")).thenReturn(inventario);

        mockMvc.perform(get("/inventario/buscarSerieExacta").param("serie", "SERIE123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroSerie").value("SERIE123"));
    }

    @Test
    void buscarPorSerieParcial() throws Exception {
        when(inventarioService.buscarPorNumeroSerieParcial("SERIE")).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarSerieParcial").param("serie", "SERIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroSerie").value("SERIE123"));
    }

    @Test
    void crearInventario_conJsonMalFormado() throws Exception {
        String malJson = "{ nombreArticulo: \"Algo sin comillas de llave }";

        mockMvc.perform(post("/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Los datos enviados son incorrectos o están mal formateados."));
    }
}
