package com.fitlife.reporte.repository;

import com.fitlife.reporte.model.reportemodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface reporterepository extends JpaRepository<reportemodel, Long> {

    List<reportemodel> findByTipo(String tipo);

    List<reportemodel> findByFechaCreacionBetween(LocalDateTime desde, LocalDateTime hasta);
}
