package com.fitlife.horario.test;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitlife.horario.controller.horariocontroller;
import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.service.horarioservice;

@WebMvcTest(horariocontroller.class)
@AutoConfigureMockMvc(addFilters = false)
public class horariocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private horarioservice horarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private horariomodel horario;

    @BeforeEach
    void setUp() {
        horario = horariomodel.builder()
                .id(1L)
                .entrenadorId(10L)
                .fechaHoraInicio(LocalDateTime.now().plusDays(1))
                .fechaHoraFin(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();
    }

    @Test
    void crearHorario_deberiaRetornarCreated() throws Exception {
        horariomodel nuevo = horariomodel.builder()
                .entrenadorId(10L)
                .fechaHoraInicio(LocalDateTime.now().plusDays(1))
                .fechaHoraFin(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();

        when(horarioService.crearHorario(any(horariomodel.class))).thenReturn(horario);

        mockMvc.perform(post("/api/horarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.entrenadorId").value(10L));
    }

    @Test
    void listarHorarios_deberiaRetornarListaConLinks() throws Exception {
        when(horarioService.getAllHorarios()).thenReturn(List.of(horario));

        mockMvc.perform(get("/api/horarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.horariomodelList[0].entrenadorId").value(10L));
    }

    @Test
    void obtenerHorarioPorId_deberiaRetornarObjeto() throws Exception {
        when(horarioService.getHorarioById(1L)).thenReturn(Optional.of(horario));

        mockMvc.perform(get("/api/horarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void obtenerHorarioPorId_noEncontrado_deberiaRetornar404() throws Exception {
        when(horarioService.getHorarioById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/horarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarHorario_deberiaActualizar() throws Exception {
        horariomodel actualizado = horariomodel.builder()
                .entrenadorId(10L)
                .fechaHoraInicio(LocalDateTime.now().plusDays(2))
                .fechaHoraFin(LocalDateTime.now().plusDays(2).plusHours(1))
                .build();

        when(horarioService.actualizarHorario(eq(1L), any(horariomodel.class))).thenReturn(horario);

        mockMvc.perform(put("/api/horarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entrenadorId").value(10L));
    }

    @Test
    void eliminarHorario_deberiaRetornarNoContent() throws Exception {
        doNothing().when(horarioService).eliminarHorario(1L);

        mockMvc.perform(delete("/api/horarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorEntrenador_deberiaRetornarLista() throws Exception {
        when(horarioService.buscarPorEntrenadorId(10L)).thenReturn(List.of(horario));

        mockMvc.perform(get("/api/horarios/buscarPorEntrenador")
                        .param("entrenadorId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].entrenadorId").value(10L));
    }

    @Test
    void buscarPorRango_deberiaRetornarLista() throws Exception {
        String desde = LocalDateTime.now().plusDays(1).toString();
        String hasta = LocalDateTime.now().plusDays(2).toString();

        when(horarioService.buscarPorRango(any(), any())).thenReturn(List.of(horario));

        mockMvc.perform(get("/api/horarios/buscarRango")
                        .param("desde", desde)
                        .param("hasta", hasta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].entrenadorId").value(10L));
    }
}
