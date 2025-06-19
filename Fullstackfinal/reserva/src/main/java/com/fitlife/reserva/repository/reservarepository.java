package com.fitlife.reserva.repository;

import com.fitlife.reserva.model.reservamodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface reservarepository extends JpaRepository<reservamodel, Long> {
}
