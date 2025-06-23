package com.fitlife.entrenador.service;

import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.repository.entrenadorrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class entrenadorservice {

    @Autowired
    private entrenadorrepository entrenadorrepository;

    public entrenador saveEntrenador(entrenador entrenador) {
        return entrenadorrepository.save(entrenador);
    }

    public List<entrenador> getAllEntrenadores() {
        return entrenadorrepository.findAll();
    }

    public entrenador getEntrenadorById(Long id) {
        return entrenadorrepository.findById(id).orElse(null);
    }

    public entrenador updateEntrenador(Long id, entrenador details) {
        return entrenadorrepository.findById(id).map(entrenador -> {
            entrenador.setNombre(details.getNombre());
            entrenador.setEspecialidad(details.getEspecialidad());
            entrenador.setExperiencia(details.getExperiencia());
            return entrenadorrepository.save(entrenador);
        }).orElse(null);
    }

    public boolean deleteEntrenador(Long id) {
        if (entrenadorrepository.existsById(id)) {
            entrenadorrepository.deleteById(id);
            return true;
        }
        return false;
    }
}
