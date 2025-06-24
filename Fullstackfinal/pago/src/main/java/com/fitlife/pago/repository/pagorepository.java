package com.fitlife.pago.repository;

import com.fitlife.pago.model.pagomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface pagorepository extends JpaRepository<pagomodel, Long> {

    List<pagomodel> findByEstadoIgnoreCase(String estado);

    List<pagomodel> findByFechaPagoBetween(LocalDate desde, LocalDate hasta);

    List<pagomodel> findByMetodoPagoIgnoreCase(String metodo);

    List<pagomodel> findByMontoGreaterThan(Double monto);

    List<pagomodel> findByMontoLessThan(Double monto);
}
