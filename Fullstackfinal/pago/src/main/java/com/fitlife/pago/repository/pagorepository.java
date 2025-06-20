package com.fitlife.pago.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fitlife.pago.model.pagomodel;

@Repository
public interface pagorepository extends JpaRepository<pagomodel, Long> {
    List<pagomodel> findByIdCliente(Long idCliente);
}