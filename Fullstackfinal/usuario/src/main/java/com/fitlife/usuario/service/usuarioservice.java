package com.fitlife.usuario.service;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.repository.usuariorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioservice {

    @Autowired
    private usuariorepository usuariorepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registro con encriptado y rol
    public usuariomodel register(usuariomodel usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuariorepository.save(usuario);
    }

    // Login: devuelve el usuario si OK, null si no OK
    public usuariomodel login(String email, String rawPassword) {
        usuariomodel user = usuariorepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        boolean ok = passwordEncoder.matches(rawPassword, user.getContrasena());
        return ok ? user : null;
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
