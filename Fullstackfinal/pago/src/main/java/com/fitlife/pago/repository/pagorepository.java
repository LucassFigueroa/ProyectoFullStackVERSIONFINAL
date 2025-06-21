package com.fitlife.pago.repository;

import com.fitlife.pago.model.pagomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface pagorepository extends JpaRepository<pagomodel, Long> {

    // Devuelve una lista de pagos filtrados por idCliente
    List<pagomodel> findByIdCliente(Long idCliente);

    // Devuelve una lista de pagos filtrados por estado (ejemplo: "Pendiente", "Pagado")
    List<pagomodel> findByEstado(String estado);

    // Devuelve pagos filtrados por idCliente Y estado al mismo tiempo
    List<pagomodel> findByIdClienteAndEstado(Long idCliente, String estado);
}
