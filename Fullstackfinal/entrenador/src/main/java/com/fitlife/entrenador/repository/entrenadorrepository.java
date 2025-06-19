package com.fitlife.entrenador.repository;

import com.fitlife.entrenador.model.entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface entrenadorrepository extends JpaRepository<entrenador, Long> {
}
