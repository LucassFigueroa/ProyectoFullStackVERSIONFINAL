package com.fitlife.notificaciones.service;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.repository.notificacionesrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class notificacionesservice {

    @Autowired
    private notificacionesrepository notificacionesRepo;

    public notificacionesmodel crearNotificacion(notificacionesmodel notif) {
        return notificacionesRepo.save(notif);
    }

    public List<notificacionesmodel> listarNotificaciones() {
        return notificacionesRepo.findAll();
    }

    public Optional<notificacionesmodel> obtenerPorId(Long id) {
        return notificacionesRepo.findById(id);
    }

    public notificacionesmodel actualizarNotificacion(Long id, notificacionesmodel notif) {
        if (!notificacionesRepo.existsById(id)) {
            throw new IllegalArgumentException("No existe ID " + id);
        }
        notif.setId(id);
        return notificacionesRepo.save(notif);
    }

    public void eliminarNotificacion(Long id) {
        if (!notificacionesRepo.existsById(id)) {
            throw new IllegalArgumentException("No existe ID " + id);
        }
        notificacionesRepo.deleteById(id);
    }

    public List<notificacionesmodel> buscarPorUsuario(Long usuarioId) {
        return notificacionesRepo.findByUsuarioId(usuarioId);
    }

    public List<notificacionesmodel> buscarPorEstado(String estado) {
        return notificacionesRepo.findByEstadoIgnoreCase(estado);
    }

    public List<notificacionesmodel> buscarPorUsuarioYEstado(Long usuarioId, String estado) {
        return notificacionesRepo.findByUsuarioIdAndEstadoIgnoreCase(usuarioId, estado);
    }
}
