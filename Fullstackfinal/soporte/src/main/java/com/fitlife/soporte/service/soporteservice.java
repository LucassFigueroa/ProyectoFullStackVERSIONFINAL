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

    public soportemodel save(soportemodel soporte) {
        return soporterepository.save(soporte);
    }

    public List<soportemodel> getAll() {
        return soporterepository.findAll();
    }

    public soportemodel getById(Long id) {
        return soporterepository.findById(id).orElse(null);
    }

    public soportemodel update(Long id, soportemodel details) {
        return soporterepository.findById(id).map(soporte -> {
            soporte.setAsunto(details.getAsunto());
            soporte.setMensaje(details.getMensaje());
            return soporterepository.save(soporte);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (soporterepository.existsById(id)) {
            soporterepository.deleteById(id);
            return true;
        }
        return false;
    }
}
