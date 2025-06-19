package com.fitlife.soporte.repository;

import com.fitlife.soporte.model.soportemodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface soporterepository extends JpaRepository<soportemodel, Long> {
}
