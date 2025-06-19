package com.fitlife.reporte.repository;

import com.fitlife.reporte.model.reportemodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface reporterepository extends JpaRepository<reportemodel, Long> {
}
