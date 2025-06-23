package com.fitlife.soporte.service;

import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.repository.soporterepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class soporteservice {

    @Autowired
    private soporterepository soporterepository;

    public soportemodel saveSoporte(soportemodel soporte) {
        return soporterepository.save(soporte);
    }

    public List<soportemodel> getAllSoporte() {
        return soporterepository.findAll();
    }

    public soportemodel getSoporteById(Long id) {
        return soporterepository.findById(id).orElse(null);
    }

    public soportemodel updateSoporte(Long id, soportemodel details) {
        return soporterepository.findById(id).map(s -> {
            s.setAsunto(details.getAsunto());
            s.setDescripcion(details.getDescripcion());
            s.setEstado(details.getEstado());
            s.setCliente(details.getCliente());
            return soporterepository.save(s);
        }).orElse(null);
    }

    public boolean deleteSoporte(Long id) {
        if (soporterepository.existsById(id)) {
            soporterepository.deleteById(id);
            return true;
        }
        return false;
    }
}
