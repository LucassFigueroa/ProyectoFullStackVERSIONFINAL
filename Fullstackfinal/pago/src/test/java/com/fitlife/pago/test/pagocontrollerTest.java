package com.fitlife.pago.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; 
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(pagocontroller.class)
@AutoConfigureMockMvc(addFilters = false)
class pagocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private pagoservice pagoService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private pagomodel pago;

    @BeforeEach
    void setUp() {
        pago = pagomodel.builder()
                .id(1L)
                .idCliente(1L)
                .monto(100.0)
                .metodoPago("Tarjeta")
                .fechaPago(LocalDate.now())
                .estado("Pendiente")
                .build();
    }

    @Test
    void testRegistrarPago() throws Exception {
        pagomodel nuevoPago = pago.toBuilder().id(null).build();
        when(pagoService.guardarPago(any(pagomodel.class))).thenReturn(pago);

        mockMvc.perform(post("/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoPago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testListarPagos() throws Exception {
        when(pagoService.obtenerPagos()).thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testObtenerPagoPorId() throws Exception {
        when(pagoService.obtenerPorId(1L)).thenReturn(Optional.of(pago));

        mockMvc.perform(get("/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testActualizarPago() throws Exception {
        when(pagoService.actualizarPago(eq(1L), any(pagomodel.class))).thenReturn(pago);

        mockMvc.perform(put("/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testEliminarPago() throws Exception {
        doNothing().when(pagoService).eliminarPago(1L);

        mockMvc.perform(delete("/pagos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPagoPorCliente() throws Exception {
        when(pagoService.obtenerPagosPorCliente(1L)).thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCliente").value(1L));
    }

    @Test
    void testPagoPorEstado() throws Exception {
        when(pagoService.obtenerPagosPorEstado("Pendiente")).thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/estado").param("estado", "Pendiente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Pendiente"));
    }

    @Test
    void testPagoPorClienteYEstado() throws Exception {
        when(pagoService.obtenerPagosPorClienteYEstado(1L, "Pendiente")).thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/clienteEstado")
                        .param("idCliente", "1")
                        .param("estado", "Pendiente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCliente").value(1L))
                .andExpect(jsonPath("$[0].estado").value("Pendiente"));
    }
}
