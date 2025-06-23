package com.fitlife.horario.service;

import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.repository.horariorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class horarioservice {

    @Autowired
    private horariorepository horarioRepository;

    // GET ALL
    public List<horariomodel> getAllHorarios() {
        return horarioRepository.findAll();
    }

    // GET BY ID
    public Optional<horariomodel> getHorarioById(Long id) {
        return horarioRepository.findById(id);
    }

    // CREATE
    public horariomodel crearHorario(horariomodel horario) {
        return horarioRepository.save(horario);
    }

    // UPDATE (ahora valida existencia)
    public horariomodel actualizarHorario(Long id, horariomodel horario) {
        if (!horarioRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el horario para actualizar.");
        }
        horario.setId(id);
        return horarioRepository.save(horario);
    }

    // DELETE (ahora valida existencia)
    public void eliminarHorario(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el horario para eliminar.");
        }
        horarioRepository.deleteById(id);
    }

    // Buscar por fecha de inicio
    public List<horariomodel> buscarPorFechaInicio(LocalDateTime fechaInicio) {
        return horarioRepository.findByFechaHoraInicio(fechaInicio);
    }

    // Buscar por fecha de fin
    public List<horariomodel> buscarPorFechaFin(LocalDateTime fechaFin) {
        return horarioRepository.findByFechaHoraFin(fechaFin);
    }

    // Buscar por rango de fechas
    public List<horariomodel> buscarPorRango(LocalDateTime desde, LocalDateTime hasta) {
        return horarioRepository.findByFechaHoraInicioBetween(desde, hasta);
    }

    // Buscar por entrenadorId
    public List<horariomodel> buscarPorEntrenadorId(Long entrenadorId) {
        return horarioRepository.findByEntrenadorId(entrenadorId);
    }

    // ✅ Buscar por entrenadorId y rango de fechas (FALTANTE)
    public List<horariomodel> buscarPorEntrenadorYRango(Long entrenadorId, LocalDateTime desde, LocalDateTime hasta) {
        return horarioRepository.findByEntrenadorIdAndFechaHoraInicioBetween(entrenadorId, desde, hasta);
    }
}
