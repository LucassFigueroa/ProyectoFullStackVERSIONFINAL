package com.fitlife.inventario.repository;

import com.fitlife.inventario.model.inventariomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface inventariorepository extends JpaRepository<inventariomodel, Long> {

    // Buscar por nombre ignora mayusculas 
    List<inventariomodel> findByNombreArticuloContainingIgnoreCase(String nombreArticulo);

    // Buscar por estado exacto 
    List<inventariomodel> findByEstadoIgnoreCase(String estado);

    // Buscar por fecha exacta de ingreso
    List<inventariomodel> findByFechaIngreso(LocalDate fechaIngreso);

    // Buscar por rango de fechas de ingreso
    List<inventariomodel> findByFechaIngresoBetween(LocalDate desde, LocalDate hasta);

    // Buscar por nombre + estado combinados
    List<inventariomodel> findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase(String nombreArticulo, String estado);
}
 