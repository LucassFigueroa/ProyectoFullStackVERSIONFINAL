package com.fitlife.pago.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.pago.controller.pagocontroller;
import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.service.pagoservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(pagocontroller.class)
@AutoConfigureMockMvc(addFilters = false)
class pagocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private pagoservice pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    private pagomodel pago;

    @BeforeEach
    void setUp() {
        pago = pagomodel.builder()
                .id(1L)
                .estado("Pagado")
                .metodoPago("Tarjeta")
                .monto(15000.0)
                .fechaPago(LocalDate.now())
                .build();
    }

    @Test
    void crearPago_ok() throws Exception {
        when(pagoService.crearPago(any())).thenReturn(pago);

        mockMvc.perform(post("/pago")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Pagado"));
    }

    @Test
    void listarPagos_ok() throws Exception {
        when(pagoService.listarPagos()).thenReturn(List.of(pago));

        mockMvc.perform(get("/pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pagomodelList[0].estado").value("Pagado"));
    }

    @Test
    void obtenerPago_porId_ok() throws Exception {
        when(pagoService.obtenerPorId(1L)).thenReturn(pago);

        mockMvc.perform(get("/pago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Pagado"));
    }

    @Test
    void actualizarPago_ok() throws Exception {
        when(pagoService.actualizarPago(eq(1L), any())).thenReturn(pago);

        mockMvc.perform(put("/pago/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Pagado"));
    }

    @Test
    void eliminarPago_ok() throws Exception {
        doNothing().when(pagoService).eliminarPago(1L);

        mockMvc.perform(delete("/pago/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorEstado_ok() throws Exception {
        when(pagoService.buscarPorEstado("Pagado")).thenReturn(List.of(pago));

        mockMvc.perform(get("/pago/buscarEstado")
                        .param("estado", "Pagado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Pagado"));
    }

    @Test
    void buscarPorRangoFecha_ok() throws Exception {
        String fecha = LocalDate.now().toString();
        when(pagoService.buscarPorRangoFecha(any(), any())).thenReturn(List.of(pago));

        mockMvc.perform(get("/pago/buscarRangoFecha")
                        .param("desde", fecha)
                        .param("hasta", fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Pagado"));
    }

    @Test
    void buscarPorMetodo_ok() throws Exception {
        when(pagoService.buscarPorMetodo("Tarjeta")).thenReturn(List.of(pago));

        mockMvc.perform(get("/pago/buscarMetodo")
                        .param("metodo", "Tarjeta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].metodoPago").value("Tarjeta"));
    }

    @Test
    void buscarPorMontoMayor_ok() throws Exception {
        when(pagoService.buscarPorMontoMayor(10000.0)).thenReturn(List.of(pago));

        mockMvc.perform(get("/pago/buscarMontoMayor")
                        .param("monto", "10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monto").value(15000.0));
    }

    @Test
    void buscarPorMontoMenor_ok() throws Exception {
        when(pagoService.buscarPorMontoMenor(20000.0)).thenReturn(List.of(pago));

        mockMvc.perform(get("/pago/buscarMontoMenor")
                        .param("monto", "20000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monto").value(15000.0));
    }
}
