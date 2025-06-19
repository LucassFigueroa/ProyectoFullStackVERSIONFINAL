package com.fitlife.usuario.controller;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.service.usuarioservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class usuariocontroller {

    @Autowired
    private usuarioservice usuarioservice;

    @PostMapping("/usuarios")
    public usuariomodel create(@Valid @RequestBody usuariomodel usuario) {
        return usuarioservice.save(usuario);
    }

    @GetMapping("/usuarios")
    public List<usuariomodel> getAll() {
        return usuarioservice.getAll();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<usuariomodel> getById(@PathVariable Long id) {
        usuariomodel usuario = usuarioservice.getById(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<usuariomodel> update(@PathVariable Long id, @Valid @RequestBody usuariomodel details) {
        usuariomodel updated = usuarioservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = usuarioservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
