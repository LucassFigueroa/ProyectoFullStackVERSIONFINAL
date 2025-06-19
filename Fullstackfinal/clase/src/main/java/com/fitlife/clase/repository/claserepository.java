package com.fitlife.clase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitlife.clase.model.clase;

@Repository
public interface claserepository extends JpaRepository<clase, Long> {
}
