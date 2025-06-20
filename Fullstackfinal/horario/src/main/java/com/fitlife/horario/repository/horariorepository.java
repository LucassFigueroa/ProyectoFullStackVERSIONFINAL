package com.fitlife.horario.repository;


import com.fitlife.horario.model.horariomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface horariorepository extends JpaRepository<horariomodel, Long> {
}
