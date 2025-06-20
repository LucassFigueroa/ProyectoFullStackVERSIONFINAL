package com.fitlife.inventario.repository;

import com.fitlife.inventario.model.inventariomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface inventariorepository extends JpaRepository<inventariomodel, Long> {
}
