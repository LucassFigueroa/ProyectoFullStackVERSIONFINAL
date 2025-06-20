package com.fitlife.inventario.service;

import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.repository.inventariorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class inventarioservice {

    @Autowired
    private inventariorepository inventarioRepository;

    public inventariomodel crearInventario(inventariomodel inventario) {
        return inventarioRepository.save(inventario);
    }

    public List<inventariomodel> listarInventarios() {
        return inventarioRepository.findAll();
    }

    public inventariomodel obtenerInventarioPorId(Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }
}
