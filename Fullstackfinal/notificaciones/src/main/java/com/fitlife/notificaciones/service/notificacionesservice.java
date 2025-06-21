package com.fitlife.notificaciones.service;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.repository.notificacionesrepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class notificacionesservice {

    private final notificacionesrepository notificacionesRepository;

    public notificacionesservice(notificacionesrepository notificacionesRepository) {
        this.notificacionesRepository = notificacionesRepository;
    }

    // Crear nueva notificacion
    public notificacionesmodel crearNotificacion(notificacionesmodel notificacion) {
        return notificacionesRepository.save(notificacion);
    }

    // Listar todas las notificaciones
    public List<notificacionesmodel> listarNotificaciones() {
        return notificacionesRepository.findAll();
    }

    // Buscar por ID
    public Optional<notificacionesmodel> buscarPorId(Long id) {
        return notificacionesRepository.findById(id);
    }

    // Actualizar notificacion existente
    public notificacionesmodel actualizarNotificacion(Long id, notificacionesmodel notificacion) {
        if (!notificacionesRepository.existsById(id)) {
            throw new IllegalArgumentException("La notificacion con ID " + id + " no existe.");
        }
        notificacion.setId(id);
        return notificacionesRepository.save(notificacion);
    }

    // Eliminar notificacion por ID
    public void eliminarNotificacion(Long id) {
        if (!notificacionesRepository.existsById(id)) {
            throw new IllegalArgumentException("La notificacion con ID " + id + " no existe.");
        }
        notificacionesRepository.deleteById(id);
    }

    // Filtrar por usuario
    public List<notificacionesmodel> buscarPorUsuarioId(Long usuarioId) {
        return notificacionesRepository.findByUsuarioId(usuarioId);
    }

    // Filtrar por estado
    public List<notificacionesmodel> buscarPorEstado(String estado) {
        return notificacionesRepository.findByEstadoIgnoreCase(estado);
    }

    // Filtrar por usuario + estado
    public List<notificacionesmodel> buscarPorUsuarioYEstado(Long usuarioId, String estado) {
        return notificacionesRepository.findByUsuarioIdAndEstadoIgnoreCase(usuarioId, estado);
    }
}
