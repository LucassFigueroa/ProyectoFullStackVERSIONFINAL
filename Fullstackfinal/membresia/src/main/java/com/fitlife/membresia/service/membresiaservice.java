package com.fitlife.membresia.service;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.repository.membresiarepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class membresiaservice {

    private final membresiarepository membresiaRepository;

    public membresiaservice(membresiarepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    // Crear nueva membresia
    public membresiamodel guardarMembresia(membresiamodel membresia) {
        return membresiaRepository.save(membresia);
    }

    // Listar todas las membresias
    public List<membresiamodel> obtenerTodas() {
        return membresiaRepository.findAll();
    }

    // Buscar membresia por ID
    public Optional<membresiamodel> obtenerPorId(Long id) {
        return membresiaRepository.findById(id);
    }

    // Actualizar membresia existente
    public membresiamodel actualizarMembresia(Long id, membresiamodel membresia) {
        if (!membresiaRepository.existsById(id)) {
            throw new IllegalArgumentException("La membresía con ID " + id + " no existe.");
        }
        membresia.setId(id);
        return membresiaRepository.save(membresia);
    }

    // Eliminar membresia por ID (cancelar o borrar física)
    public void eliminarMembresia(Long id) {
        if (!membresiaRepository.existsById(id)) {
            throw new IllegalArgumentException("La membresía con ID " + id + " no existe.");
        }
        membresiaRepository.deleteById(id);
    }

    // Filtrar por usuario
    public List<membresiamodel> buscarPorUsuario(Long usuarioId) {
        return membresiaRepository.findByUsuarioId(usuarioId);
    }

    // Filtrar por estado (Activa, Cancelada, Expirada)
    public List<membresiamodel> buscarPorEstado(String estado) {
        return membresiaRepository.findByEstadoIgnoreCase(estado);
    }

    // Filtrar por usuario + estado
    public List<membresiamodel> buscarPorUsuarioYEstado(Long usuarioId, String estado) {
        return membresiaRepository.findByUsuarioIdAndEstadoIgnoreCase(usuarioId, estado);
    }
}
