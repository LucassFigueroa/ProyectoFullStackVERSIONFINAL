package com.fitlife.reserva.repository;

import com.fitlife.reserva.model.reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface reservarepository extends JpaRepository<reserva, Long> {


    List<reserva> findByClienteNombre(String clienteNombre);

    List<reserva> findByFecha(LocalDate fecha);

    List<reserva> findByFechaBetween(LocalDate desde, LocalDate hasta);

    boolean existsByClienteNombreAndFechaAndHora(String clienteNombre, LocalDate fecha, LocalTime hora);
}
