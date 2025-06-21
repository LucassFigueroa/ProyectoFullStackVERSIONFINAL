package com.fitlife.entrenador.service;

import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.repository.entrenadorrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class entrenadorservice {

    @Autowired
    private entrenadorrepository entrenadorrepository;

    // Crear
    public entrenador crearEntrenador(entrenador entrenador) {
        return entrenadorrepository.save(entrenador);
    }

    // Obtener uno por ID
    public Optional<entrenador> obtenerEntrenadorPorId(Long id) {
        return entrenadorrepository.findById(id);
    }

    // Obtener todos
    public List<entrenador> obtenerTodos() {
        return entrenadorrepository.findAll();
    }

    // Actualizar
    public entrenador actualizarEntrenador(Long id, entrenador detalles) {
        return entrenadorrepository.findById(id).map(entrenador -> {
            entrenador.setNombre(detalles.getNombre());
            entrenador.setEspecialidad(detalles.getEspecialidad());
            // agrega otros campos si tienes más
            return entrenadorrepository.save(entrenador);
        }).orElse(null);
    }

    // Eliminar
    public boolean eliminarEntrenador(Long id) {
        if (entrenadorrepository.existsById(id)) {
            entrenadorrepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
