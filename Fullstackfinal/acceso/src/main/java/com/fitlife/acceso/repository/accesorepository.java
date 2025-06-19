package com.fitlife.acceso.repository;

import com.fitlife.acceso.model.accesomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface accesorepository extends JpaRepository<accesomodel, Long> {
}
