package com.fitlife.usuario.service;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.repository.usuariorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioservice {

    @Autowired
    private usuariorepository usuariorepository;

    public usuariomodel save(usuariomodel usuario) {
        return usuariorepository.save(usuario);
    }

    public List<usuariomodel> getAll() {
        return usuariorepository.findAll();
    }

    public usuariomodel getById(Long id) {
        return usuariorepository.findById(id).orElse(null);
    }

    public usuariomodel update(Long id, usuariomodel details) {
        return usuariorepository.findById(id).map(usuario -> {
            usuario.setNombre(details.getNombre());
            usuario.setEmail(details.getEmail());
            usuario.setContrasena(details.getContrasena());
            return usuariorepository.save(usuario);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (usuariorepository.existsById(id)) {
            usuariorepository.deleteById(id);
            return true;
        }
        return false;
    }

    public usuariomodel findByEmail(String email) {
        return usuariorepository.findByEmail(email);
    }
}
