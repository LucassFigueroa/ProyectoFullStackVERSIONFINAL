package com.fitlife.horario.repository;

import com.fitlife.horario.model.horariomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface horariorepository extends JpaRepository<horariomodel, Long> {

    // Filtrar todos los horarios de un entrenador específico
    List<horariomodel> findByEntrenadorId(Long entrenadorId);

    // Filtrar horarios entre un rango de fecha/hora de inicio
    List<horariomodel> findByFechaHoraInicioBetween(LocalDateTime desde, LocalDateTime hasta);

    // Filtrar horarios por entrenador y rango de fecha/hora
    List<horariomodel> findByEntrenadorIdAndFechaHoraInicioBetween(Long entrenadorId, LocalDateTime desde, LocalDateTime hasta);
}