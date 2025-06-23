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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .nombreArticulo("Máquina de Pecho Horizontal Profitness")
                .numeroSerie("PF-PH-2024")
                .cantidad(5)
                .estado("Funcional")
                .fechaIngreso(LocalDate.now())
                .build();
    }

    @Test
    void crearInventario_deberiaRetornarOk() throws Exception {
        inventariomodel nuevo = inventariomodel.builder()
                .nombreArticulo("Máquina de Pecho Horizontal Profitness")
                .numeroSerie("PF-PH-2024")
                .cantidad(5)
                .estado("Funcional")
                .fechaIngreso(LocalDate.now())
                .build();

        when(inventarioService.crearInventario(any(inventariomodel.class)))
                .thenReturn(inventario);

        mockMvc.perform(post("/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho Horizontal Profitness"))
                .andExpect(jsonPath("$.numeroSerie").value("PF-PH-2024"))
                .andExpect(jsonPath("$.cantidad").value(5))
                .andExpect(jsonPath("$.estado").value("Funcional"));
    }

    @Test
    void listarInventarios_deberiaRetornarLista() throws Exception {
        when(inventarioService.listarInventarios()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho Horizontal Profitness"));
    }

    @Test
    void obtenerPorId_deberiaRetornarObjeto() throws Exception {
        when(inventarioService.obtenerPorId(1L)).thenReturn(inventario);

        mockMvc.perform(get("/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroSerie").value("PF-PH-2024"));
    }

    @Test
    void obtenerPorId_conIdInvalido() throws Exception {
        mockMvc.perform(get("/inventario/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El ID proporcionado no es válido"));
    }

    @Test
    void actualizarInventario_deberiaActualizarCorrectamente() throws Exception {
        inventariomodel actualizado = inventariomodel.builder()
                .nombreArticulo("Máquina de Pecho Horizontal Profitness Actualizada")
                .numeroSerie("PF-PH-2024-ACT")
                .cantidad(8)
                .estado("Funcional")
                .fechaIngreso(LocalDate.now())
                .build();

        when(inventarioService.actualizarInventario(eq(1L), any(inventariomodel.class)))
                .thenReturn(inventariomodel.builder()
                        .id(1L)
                        .nombreArticulo("Máquina de Pecho Horizontal Profitness Actualizada")
                        .numeroSerie("PF-PH-2024-ACT")
                        .cantidad(8)
                        .estado("Funcional")
                        .fechaIngreso(LocalDate.now())
                        .build());

        mockMvc.perform(put("/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArticulo").value("Máquina de Pecho Horizontal Profitness Actualizada"))
                .andExpect(jsonPath("$.numeroSerie").value("PF-PH-2024-ACT"))
                .andExpect(jsonPath("$.cantidad").value(8));
    }

    @Test
    void eliminarInventario_deberiaRetornarNoContent() throws Exception {
        doNothing().when(inventarioService).eliminarInventario(1L);

        mockMvc.perform(delete("/inventario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorNombre_deberiaRetornarCoincidencias() throws Exception {
        when(inventarioService.buscarPorNombre("Pecho")).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarNombre")
                        .param("nombre", "Pecho"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho Horizontal Profitness"));
    }

    @Test
    void buscarPorEstado_deberiaRetornarCoincidencias() throws Exception {
        when(inventarioService.buscarPorEstado("Funcional")).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarEstado")
                        .param("estado", "Funcional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Funcional"));
    }

    @Test
    void buscarPorFecha_deberiaRetornarCoincidencias() throws Exception {
        String fecha = LocalDate.now().toString();
        when(inventarioService.buscarPorFecha(LocalDate.parse(fecha)))
                .thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarFecha")
                        .param("fecha", fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho Horizontal Profitness"));
    }

    @Test
    void buscarPorRangoFecha_deberiaRetornarCoincidencias() throws Exception {
        String fecha = LocalDate.now().toString();
        when(inventarioService.buscarPorRangoFecha(LocalDate.parse(fecha), LocalDate.parse(fecha)))
                .thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarRango")
                        .param("desde", fecha)
                        .param("hasta", fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho Horizontal Profitness"));
    }

    @Test
    void buscarPorNombreYEstado_deberiaRetornarCoincidencias() throws Exception {
        when(inventarioService.buscarPorNombreYEstado("Pecho", "Funcional"))
                .thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscarAvanzado")
                        .param("nombre", "Pecho")
                        .param("estado", "Funcional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreArticulo").value("Máquina de Pecho Horizontal Profitness"))
                .andExpect(jsonPath("$[0].estado").value("Funcional"));
    }

    @Test
    void buscarPorNumeroSerieExacto_deberiaRetornarCoincidencias() throws Exception {
        when(inventarioService.buscarPorNumeroSerie("PF-PH-2024"))
                .thenReturn(inventario);

        mockMvc.perform(get("/inventario/buscarSerieExacta")
                        .param("serie", "PF-PH-2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroSerie").value("PF-PH-2024"));
    }

    @Test
    void crearInventario_conJsonMalFormado_deberiaRetornarBadRequest() throws Exception {
        String malJson = "{ nombreArticulo: \"Máquina de Pecho Horizontal Profitness\" ";

        mockMvc.perform(post("/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Los datos enviados son incorrectos o están mal formateados."));
    }
}
