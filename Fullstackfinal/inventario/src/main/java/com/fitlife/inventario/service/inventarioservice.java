package com.fitlife.inventario.service;

import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.repository.inventariorepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class inventarioservice {

    private final inventariorepository inventarioRepository;

    // Inyección por constructor
    public inventarioservice(inventariorepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    // Crear nuevo inventario
    public inventariomodel crearInventario(inventariomodel inventario) {
        return inventarioRepository.save(inventario);
    }

    // Listar todos los registros
    public List<inventariomodel> listarInventarios() {
        return inventarioRepository.findAll();
    }

    // Buscar uno por ID
    public inventariomodel obtenerPorId(Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    // Actualizar inventario existente
    public inventariomodel actualizarInventario(Long id, inventariomodel inventario) {
        if (!inventarioRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el ID " + id);
        }
        inventario.setId(id); // Forzar coherencia con la URL
        return inventarioRepository.save(inventario);
    }

    // Eliminar inventario por ID
    public void eliminarInventario(Long id) {
        inventarioRepository.deleteById(id);
    }

    // Buscar por nombre parcial
    public List<inventariomodel> buscarPorNombre(String nombre) {
        return inventarioRepository.findByNombreArticuloContainingIgnoreCase(nombre);
    }

    // Buscar por estado
    public List<inventariomodel> buscarPorEstado(String estado) {
        return inventarioRepository.findByEstadoIgnoreCase(estado);
    }

    // Buscar por fecha exacta
    public List<inventariomodel> buscarPorFecha(LocalDate fecha) {
        return inventarioRepository.findByFechaIngreso(fecha);
    }

    // Buscar por rango de fechas
    public List<inventariomodel> buscarPorRangoFecha(LocalDate desde, LocalDate hasta) {
        return inventarioRepository.findByFechaIngresoBetween(desde, hasta);
    }

    // Buscar por nombre + estado
    public List<inventariomodel> buscarPorNombreYEstado(String nombre, String estado) {
        return inventarioRepository.findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase(nombre, estado);
    }

    // Buscar uno por número de serie exacto
    public inventariomodel buscarPorNumeroSerie(String numeroSerie) {
        return inventarioRepository.findByNumeroSerie(numeroSerie);
    }

    // Buscar lista por coincidencia parcial de número de serie
    public List<inventariomodel> buscarPorNumeroSerieParcial(String numeroSerie) {
        return inventarioRepository.findByNumeroSerieContainingIgnoreCase(numeroSerie);
    }
}
