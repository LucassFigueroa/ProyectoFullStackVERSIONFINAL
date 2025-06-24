package com.fitlife.inventario.repository;

import com.fitlife.inventario.model.inventariomodel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface inventariorepository extends JpaRepository<inventariomodel, Long> {
    List<inventariomodel> findByNombreArticuloContainingIgnoreCase(String nombre);
    List<inventariomodel> findByEstadoIgnoreCase(String estado);
    List<inventariomodel> findByFechaIngreso(LocalDate fecha);
    List<inventariomodel> findByFechaIngresoBetween(LocalDate desde, LocalDate hasta);
    List<inventariomodel> findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase(String nombre, String estado);
    Optional<inventariomodel> findByNumeroSerie(String serie);
    List<inventariomodel> findByNumeroSerieContainingIgnoreCase(String serie);
}
