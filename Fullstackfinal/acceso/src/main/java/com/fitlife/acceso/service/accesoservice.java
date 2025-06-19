package com.fitlife.acceso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitlife.acceso.model.accesomodel;
import com.fitlife.acceso.repository.accesorepository;

import java.util.List;

@Service
public class accesoservice {

    @Autowired
    private accesorepository accesoRepository;

    public accesomodel saveAcceso(accesomodel acceso) {
        return accesoRepository.save(acceso);
    }

    public List<accesomodel> getAllAccesos() {
        return accesoRepository.findAll();
    }

    public accesomodel getAccesoById(Long id) {
        return accesoRepository.findById(id).orElse(null);
    }

    public accesomodel updateAcceso(Long id, accesomodel accesoDetails) {
        return accesoRepository.findById(id).map(acceso -> {
            acceso.setNombreUsuario(accesoDetails.getNombreUsuario());
            acceso.setContrasena(accesoDetails.getContrasena());
            return accesoRepository.save(acceso);
        }).orElse(null);
    }

    public boolean deleteAcceso(Long id) {
        if (accesoRepository.existsById(id)) {
            accesoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
