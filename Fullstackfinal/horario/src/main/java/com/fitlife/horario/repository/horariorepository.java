package com.fitlife.horario.repository;

import com.fitlife.horario.model.horariomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface horariorepository extends JpaRepository<horariomodel, Long> {

    List<horariomodel> findByFechaHoraInicio(LocalDateTime fechaHoraInicio);

    List<horariomodel> findByFechaHoraFin(LocalDateTime fechaHoraFin);

    List<horariomodel> findByFechaHoraInicioBetween(LocalDateTime desde, LocalDateTime hasta);

    List<horariomodel> findByEntrenadorId(Long entrenadorId);

    List<horariomodel> findByEntrenadorIdAndFechaHoraInicioBetween(Long entrenadorId, LocalDateTime desde, LocalDateTime hasta);
}
