package com.fitlife.inventario.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.inventario.controller.inventariocontroller;
import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.service.inventarioservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(inventariocontroller.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva seguridad para test unitario
public class inventariocontrollerTest {

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
                .estado("Operativo")
                .fechaIngreso(LocalDate.now())
                .build();
    }

    @Test
    void crearInventario_OK() throws Exception {
        // Objeto de entrada SIN ID
        inventariomodel nuevo = inventariomodel.builder()
                .nombreArticulo("Máquina de Pecho")
                .numeroSerie("SERIE123")
                .cantidad(5)
                .estado("Operativo")
                .fechaIngreso(LocalDate.now())
                .build();

        // El servicio devuelve uno CON ID
        when(inventarioService.crearInventario(any())).thenReturn(inventario);

        mockMvc.perform(post("/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho"))
                .andExpect(jsonPath("$.numeroSerie").value("SERIE123"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void listarInventarios_OK() throws Exception {
        when(inventarioService.listarInventarios()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.inventariomodelList[0].nombreArticulo").value("Máquina de Pecho"))
                .andExpect(jsonPath("$._embedded.inventariomodelList[0].numeroSerie").value("SERIE123"))
                .andExpect(jsonPath("$._embedded.inventariomodelList[0]._links.self.href").exists());
    }

    @Test
    void obtenerInventario_OK() throws Exception {
        when(inventarioService.obtenerPorId(1L)).thenReturn(inventario);

        mockMvc.perform(get("/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho"))
                .andExpect(jsonPath("$.numeroSerie").value("SERIE123"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void actualizarInventario_OK() throws Exception {
        inventariomodel actualizado = inventariomodel.builder()
                .id(1L)
                .nombreArticulo("Máquina de Pecho Modificada")
                .numeroSerie("SERIE123")
                .cantidad(7)
                .estado("Operativo")
                .fechaIngreso(LocalDate.now())
                .build();

        when(inventarioService.actualizarInventario(any(), any())).thenReturn(actualizado);

        mockMvc.perform(put("/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho Modificada"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void eliminarInventario_OK() throws Exception {
        mockMvc.perform(delete("/inventario/1"))
                .andExpect(status().isNoContent());
    }
}
