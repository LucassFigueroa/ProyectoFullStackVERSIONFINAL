package com.fitlife.inventario.service;

import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.repository.inventariorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class inventarioservice {

    @Autowired
    private inventariorepository repo;

    public inventariomodel crearInventario(inventariomodel inventario) {
        return repo.save(inventario);
    }

    public List<inventariomodel> listarInventarios() {
        return repo.findAll();
    }

    public inventariomodel obtenerPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public inventariomodel actualizarInventario(Long id, inventariomodel inventario) {
        return repo.findById(id).map(existing -> {
            existing.setNombreArticulo(inventario.getNombreArticulo());
            existing.setCantidad(inventario.getCantidad());
            existing.setEstado(inventario.getEstado());
            existing.setNumeroSerie(inventario.getNumeroSerie());
            existing.setFechaIngreso(inventario.getFechaIngreso());
            return repo.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("No se encontró el inventario con ID " + id));
    }

    public void eliminarInventario(Long id) {
        repo.deleteById(id);
    }

    public List<inventariomodel> buscarPorNombre(String nombre) {
        return repo.findByNombreArticuloContainingIgnoreCase(nombre);
    }

    public List<inventariomodel> buscarPorEstado(String estado) {
        return repo.findByEstadoIgnoreCase(estado);
    }

    public List<inventariomodel> buscarPorFecha(LocalDate fecha) {
        return repo.findByFechaIngreso(fecha);
    }

    public List<inventariomodel> buscarPorRangoFecha(LocalDate desde, LocalDate hasta) {
        return repo.findByFechaIngresoBetween(desde, hasta);
    }

    public List<inventariomodel> buscarPorNombreYEstado(String nombre, String estado) {
        return repo.findByNombreArticuloContainingIgnoreCaseAndEstadoIgnoreCase(nombre, estado);
    }

    public inventariomodel buscarPorNumeroSerie(String serie) {
        return repo.findByNumeroSerie(serie).orElse(null);
    }

    public List<inventariomodel> buscarPorNumeroSerieParcial(String serie) {
        return repo.findByNumeroSerieContainingIgnoreCase(serie);
    }
}
