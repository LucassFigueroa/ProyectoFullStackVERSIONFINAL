package com.fitlife.horario.service;

import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.repository.horariorepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class horarioservice {

    private final horariorepository horarioRepository;

    public horarioservice(horariorepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    // Crea un nuevo horario teniendo coherencia  en las fechas
    public horariomodel guardarHorario(horariomodel horario) {
        validarFechas(horario);
        return horarioRepository.save(horario);
    }

    // Lista todos los Horarios
    public List<horariomodel> listarHorarios() {
        return horarioRepository.findAll();
    }

    // Busca por Id
    public Optional<horariomodel> obtenerPorId(Long id) {
        return horarioRepository.findById(id);
    }

    //  modifica horarios 
    public horariomodel actualizarHorario(Long id, horariomodel horario) {
        if (!horarioRepository.existsById(id)) {
            throw new IllegalArgumentException("El horario con ID " + id + " no existe");
        }
        validarFechas(horario);
        horario.setId(id);
        return horarioRepository.save(horario);
    }

    // elimina horarios 
    public void eliminarHorario(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new IllegalArgumentException("El horario con ID " + id + " no existe");
        }
        horarioRepository.deleteById(id);
    }

    // Filtra por id del entrenador 
    public List<horariomodel> buscarPorEntrenador(Long entrenadorId) {
        return horarioRepository.findByEntrenadorId(entrenadorId);
    }

    // Filtra por rango de fecha y hora de inicio 
    public List<horariomodel> buscarPorRangoFechaHora(LocalDateTime desde, LocalDateTime hasta) {
        return horarioRepository.findByFechaHoraInicioBetween(desde, hasta);
    }

    // Filtrar por entrenador y rango de fecha/hora
    public List<horariomodel> buscarPorEntrenadorYRango(Long entrenadorId, LocalDateTime desde, LocalDateTime hasta) {
        return horarioRepository.findByEntrenadorIdAndFechaHoraInicioBetween(entrenadorId, desde, hasta);
    }

  
    // Valida que la fecha de inicio no sea posterior a la fecha de fin
    private void validarFechas(horariomodel horario) {
        if (horario.getFechaHoraInicio().isAfter(horario.getFechaHoraFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }
}