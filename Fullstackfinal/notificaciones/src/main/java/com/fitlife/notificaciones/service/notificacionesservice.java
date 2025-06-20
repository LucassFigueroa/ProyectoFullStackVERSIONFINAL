package com.fitlife.notificaciones.service;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.repository.notificacionesrepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class notificacionesservice{

    private final notificacionesrepository notificacionesRepository ;

    public notificacionesservice(notificacionesrepository notificacionesRepository) {
        this.notificacionesRepository = notificacionesRepository;
    }

    public notificacionesmodel crearNotificacion(notificacionesmodel notificacion) {
        return notificacionesRepository.save(notificacion);
    }

    public List<notificacionesmodel> listarNotificaciones() {
        return notificacionesRepository.findAll();
    }

    public List<notificacionesmodel> buscarPorUsuarioId(Long usuarioId) {
        return notificacionesRepository.findByUsuarioId(usuarioId);
    }

}
