package com.fitlife.membresia.repository;

import com.fitlife.membresia.model.membresiamodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface membresiarepository extends JpaRepository<membresiamodel, Long> {

    // Filtrar membresías de un usuario específico
    List<membresiamodel> findByUsuarioId(Long usuarioId);

    // Filtrar membresías por estado (Activa, Cancelada, Expirada)
    List<membresiamodel> findByEstadoIgnoreCase(String estado);

    // Filtrar membresías de un usuario por estado 
    List<membresiamodel> findByUsuarioIdAndEstadoIgnoreCase(Long usuarioId, String estado);
}
