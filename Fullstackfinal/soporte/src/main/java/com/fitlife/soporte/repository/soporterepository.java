package com.fitlife.soporte.repository;

import com.fitlife.soporte.model.soportemodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface soporterepository extends JpaRepository<soportemodel, Long> {
    List<soportemodel> findByAsuntoContainingIgnoreCase(String keyword); // filtro por palabra clave
}
