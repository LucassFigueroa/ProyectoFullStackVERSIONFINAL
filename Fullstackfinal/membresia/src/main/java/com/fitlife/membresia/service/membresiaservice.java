package com.fitlife.membresia.service;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.repository.membresiarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class membresiaservice {

    @Autowired
    private membresiarepository membresiarepository;

    public membresiamodel saveMembresia(membresiamodel membresia) {
        return membresiarepository.save(membresia);
    }

    public List<membresiamodel> getAllMembresias() {
        return membresiarepository.findAll();
    }

    public membresiamodel getMembresiaById(Long id) {
        return membresiarepository.findById(id).orElse(null);
    }

    public membresiamodel updateMembresia(Long id, membresiamodel details) {
        return membresiarepository.findById(id).map(m -> {
            m.setUsuarioId(details.getUsuarioId());
            m.setTipo(details.getTipo());
            m.setFechaInicio(details.getFechaInicio());
            m.setFechaFin(details.getFechaFin());
            m.setEstado(details.getEstado());
            return membresiarepository.save(m);
        }).orElse(null);
    }

    public boolean deleteMembresia(Long id) {
        if (membresiarepository.existsById(id)) {
            membresiarepository.deleteById(id);
            return true;
        }
        return false;
    }
}
