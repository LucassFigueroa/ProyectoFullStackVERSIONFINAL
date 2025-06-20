package com.fitlife.notificaciones.repository;

import com.fitlife.notificaciones.model.notificacionesmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface notificacionesrepository extends JpaRepository<notificacionesmodel, Long> {
    List<notificacionesmodel> findByUsuarioId(Long usuarioId);

}
  