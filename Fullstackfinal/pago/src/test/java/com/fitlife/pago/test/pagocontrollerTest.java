package com.fitlife.pago.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.pago.controller.pagocontroller;
import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.service.pagoservice;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(pagocontroller.class)
public class pagocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private pagoservice pagoservice;

    @Autowired
    private ObjectMapper objectMapper;

    private pagomodel crearPagoEjemplo() {
        return new pagomodel(1L, 100L, 2000, "Transferencia", LocalDate.now(), "Pagado");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "STAFF"})
    public void testCrearPago() throws Exception {
        pagomodel pago = crearPagoEjemplo();
        when(pagoservice.crearPago(any(pagomodel.class))).thenReturn(pago);

        mockMvc.perform(post("/pago")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.metodoPago", is("Transferencia")));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "STAFF"})
    public void testListarPagos() throws Exception {
        pagomodel pago1 = crearPagoEjemplo();
        pagomodel pago2 = new pagomodel(2L, 101L, 1500, "Tarjeta", LocalDate.now(), "Pendiente");
        List<pagomodel> lista = Arrays.asList(pago1, pago2);
        when(pagoservice.listarPagos()).thenReturn(lista);

        mockMvc.perform(get("/pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pagomodelList.length()").value(2));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "STAFF", "USER"})
    public void testObtenerPagoPorId() throws Exception {
        pagomodel pago = crearPagoEjemplo();
        when(pagoservice.obtenerPorId(1L)).thenReturn(pago);

        mockMvc.perform(get("/pago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("Pagado")));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "STAFF"})
    public void testActualizarPago() throws Exception {
        pagomodel pagoActualizado = new pagomodel(1L, 100L, 3000, "Efectivo", LocalDate.now(), "Pendiente");

        when(pagoservice.obtenerPorId(1L)).thenReturn(pagoActualizado);
        when(pagoservice.actualizarPago(eq(1L), any(pagomodel.class))).thenReturn(pagoActualizado);

        mockMvc.perform(put("/pago/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto", is(3000)))
                .andExpect(jsonPath("$.estado", is("Pendiente")));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testEliminarPago() throws Exception {
        pagomodel pago = crearPagoEjemplo();
        when(pagoservice.obtenerPorId(1L)).thenReturn(pago);
        Mockito.doNothing().when(pagoservice).eliminarPago(1L);

        mockMvc.perform(delete("/pago/1"))
                .andExpect(status().isNoContent());
    }
}
