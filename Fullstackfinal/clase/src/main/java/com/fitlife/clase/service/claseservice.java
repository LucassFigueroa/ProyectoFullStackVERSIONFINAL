package com.fitlife.clase.service;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.repository.claserepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class claseservice {

    @Autowired
    private claserepository claseRepository;

    public clase saveClase(clase clase) {
        return claseRepository.save(clase);
    }

    public List<clase> getAllClases() {
        return claseRepository.findAll();
    }

    public clase getClaseById(Long id) {
        return claseRepository.findById(id).orElse(null);
    }

    public clase updateClase(Long id, clase claseDetails) {
        return claseRepository.findById(id).map(clase -> {
            clase.setNombreClase(claseDetails.getNombreClase());
            clase.setDescripcion(claseDetails.getDescripcion());
            return claseRepository.save(clase);
        }).orElse(null);
    }

    public boolean deleteClase(Long id) {
        if (claseRepository.existsById(id)) {
            claseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
