package com.fitlife.usuario.service;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.repository.usuariorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioservice {

    private final usuariorepository usuariorepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public usuarioservice(usuariorepository usuariorepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuariorepository = usuariorepository;
        this.passwordEncoder = passwordEncoder;
    }

    public usuariomodel register(usuariomodel usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuariorepository.save(usuario);
    }

    public usuariomodel login(String email, String rawPassword) {
        usuariomodel user = usuariorepository.findByEmail(email);
        if (user == null) return null;
        return passwordEncoder.matches(rawPassword, user.getContrasena()) ? user : null;
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
            usuario.setRol(details.getRol());
            usuario.setContrasena(passwordEncoder.encode(details.getContrasena()));
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
    }
