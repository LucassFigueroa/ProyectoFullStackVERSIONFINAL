package com.fitlife.clase.service;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.repository.claserepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class claseservice {

    @Autowired
    private claserepository claserepository;

    public clase guardarClase(clase clase) {
        return claserepository.save(clase);
    }

    public Optional<clase> obtenerClasePorId(Long id) {
        return claserepository.findById(id);
    }

    public List<clase> obtenerTodasLasClases() {
        return claserepository.findAll();
    }

    public clase actualizarClase(Long id, clase claseDetails) {
        return claserepository.findById(id).map(clase -> {
            clase.setNombreClase(claseDetails.getNombreClase());
            clase.setDescripcion(claseDetails.getDescripcion());
            clase.setDuracion(claseDetails.getDuracion());
            clase.setCapacidadMaxima(claseDetails.getCapacidadMaxima());
            clase.setNivel(claseDetails.getNivel());
            clase.setEntrenadorId(claseDetails.getEntrenadorId());
            return claserepository.save(clase);
        }).orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + id));
    }

    public void eliminarClase(Long id) {
        claserepository.deleteById(id);
    }
}
