package com.fitlife.notificaciones.repository;

import com.fitlife.notificaciones.model.notificacionesmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface notificacionesrepository extends JpaRepository<notificacionesmodel, Long> {

    // Filtrar notificaciones por usuario
    List<notificacionesmodel> findByUsuarioId(Long usuarioId);

    // Filtrar notificaciones por estado (Leida, No Leida)
    List<notificacionesmodel> findByEstadoIgnoreCase(String estado);

    // Filtrar notificaciones por usuario + estado
    List<notificacionesmodel> findByUsuarioIdAndEstadoIgnoreCase(Long usuarioId, String estado);
}
