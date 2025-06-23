package com.fitlife.inventario.repository;

import com.fitlife.inventario.model.inventariomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface inventariorepository extends JpaRepository<inventariomodel, Long> {

    // Buscar uno por número de serie exacto
    inventariomodel findByNumeroSerie(String numeroSerie);

    // Buscar lista por coincidencia parcial de número de serie
    List<inventariomodel> findByNumeroSerieContainingIgnoreCase(String numeroSerie);

    // Buscar por nombre ignora mayúsculas
    List<inventariomodel> findByNombreArticuloContainingIgnoreCase(String nombreArticulo);

    // Buscar por estado exacto
    List<inventariomodel> findByEstadoIgnoreCase(String estado);

    // Buscar por fecha exacta
    List<inventariomodel> findByFechaIngreso(LocalDate fechaIngreso);

    // Buscar por rango de fechas
    List<inventariomodel> findByFechaIngresoBetween(LocalDate desde, LocalDate hasta);

    // Buscar por nombre + estado
    List<inventariomodel> findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase(String nombreArticulo, String estado);
}
