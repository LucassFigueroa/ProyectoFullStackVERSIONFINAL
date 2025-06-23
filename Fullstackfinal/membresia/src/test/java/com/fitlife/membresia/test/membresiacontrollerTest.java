package com.fitlife.membresia.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.membresia.controller.membresiacontroller;
import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.service.membresiaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(membresiacontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class membresiacontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private membresiaservice membresiaService;

    @Autowired
    private ObjectMapper objectMapper;

    private membresiamodel membresia;

    @BeforeEach
    void setUp() {
        membresia = membresiamodel.builder()
                .id(1L)
                .usuarioId(123L)
                .tipo("Premium")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusMonths(1))
                .estado("Activa")
                .build();
    }

    @Test
    void crearMembresia_deberiaRetornarOk() throws Exception {
        membresiamodel nuevo = membresiamodel.builder()
                .usuarioId(123L)
                .tipo("Premium")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusMonths(1))
                .estado("Activa")
                .build();

        when(membresiaService.guardarMembresia(any(membresiamodel.class))).thenReturn(membresia);

        mockMvc.perform(post("/membresias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuarioId").value(123L))
                .andExpect(jsonPath("$.tipo").value("Premium"));
    }

    @Test
    void listarMembresias_deberiaRetornarLista() throws Exception {
        when(membresiaService.obtenerTodas()).thenReturn(List.of(membresia));

        mockMvc.perform(get("/membresias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerPorId_deberiaRetornarObjeto() throws Exception {
        when(membresiaService.obtenerPorId(1L)).thenReturn(Optional.of(membresia));

        mockMvc.perform(get("/membresias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Premium"));
    }

    @Test
    void obtenerPorId_noExiste_deberiaRetornarNotFound() throws Exception {
        when(membresiaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/membresias/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Membresía no encontrada"));
    }

    @Test
    void actualizarMembresia_deberiaRetornarOk() throws Exception {
        membresiamodel actualizado = membresiamodel.builder()
                .usuarioId(123L)
                .tipo("Premium Plus")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusMonths(2))
                .estado("Activa")
                .build();

        when(membresiaService.actualizarMembresia(eq(1L), any(membresiamodel.class))).thenReturn(actualizado);

        mockMvc.perform(put("/membresias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Premium Plus"));
    }

    @Test
    void eliminarMembresia_deberiaRetornarNoContent() throws Exception {
        Mockito.doNothing().when(membresiaService).eliminarMembresia(1L);

        mockMvc.perform(delete("/membresias/1"))
                .andExpect(status().isNoContent());
    }
}
