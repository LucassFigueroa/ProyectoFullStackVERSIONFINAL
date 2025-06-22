package com.fitlife.reserva.repository;

import com.fitlife.reserva.model.reservamodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface reservarepository extends JpaRepository<reservamodel, Long> {

    List<reservamodel> findByUsuarioId(Long usuarioId);
    List<reservamodel> findByFecha(LocalDate fecha);
    List<reservamodel> findByFechaBetween(LocalDate desde, LocalDate hasta);
    boolean existsByUsuarioIdAndFechaAndHora(Long usuarioId, LocalDate fecha, LocalTime hora);
}

