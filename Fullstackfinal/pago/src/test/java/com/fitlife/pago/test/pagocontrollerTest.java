package com.fitlife.pago.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.pago.controller.pagocontroller;
import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.service.pagoservice;
import com.fitlife.pago.config.securityconfig; // importa tu clase real aquí
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(pagocontroller.class)
@Import(securityconfig.class) // 🔥 Esto activa tu configuración real de seguridad
public class pagocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private pagoservice pagoservice;

    private pagomodel crearPagoMock() {
        return pagomodel.builder()
                .id(1L)
                .idCliente(123L)
                .monto(15000)
                .metodoPago("Transferencia")
                .fechaPago(LocalDate.of(2025, 7, 6))
                .estado("Pagado")
                .build();
    }

    @WithMockUser(roles = "STAFF")
    @Test
    public void testCrearPago() throws Exception {
        pagomodel pago = crearPagoMock();
        when(pagoservice.crearPago(any())).thenReturn(pago);

        mockMvc.perform(post("/pago")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = "STAFF")
    @Test
    public void testActualizarPago() throws Exception {
        pagomodel pago = crearPagoMock();
        when(pagoservice.obtenerPorId(1L)).thenReturn(pago);
        when(pagoservice.actualizarPago(eq(1L), any())).thenReturn(pago);

        mockMvc.perform(put("/pago/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "SOPORTE")
    @Test
    public void testObtenerPagoPorId() throws Exception {
        pagomodel pago = crearPagoMock();
        when(pagoservice.obtenerPorId(1L)).thenReturn(pago);

        mockMvc.perform(get("/pago/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testEliminarPago() throws Exception {
        pagomodel pago = crearPagoMock();
        when(pagoservice.obtenerPorId(1L)).thenReturn(pago);
        doNothing().when(pagoservice).eliminarPago(1L);

        mockMvc.perform(delete("/pago/1"))
                .andExpect(status().isNoContent());
    }
}
