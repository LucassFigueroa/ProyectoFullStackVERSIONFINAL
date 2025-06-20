package com.fitlife.usuario.controller;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.service.usuarioservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class usuariocontroller {

    @Autowired
    private usuarioservice usuarioservice;

    @PostMapping("/register")
    public usuariomodel register(@Valid @RequestBody usuariomodel usuario) {
        return usuarioservice.register(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String contrasena = loginData.get("contrasena");

        usuariomodel user = usuarioservice.login(email, contrasena);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login OK ✅");
            response.put("id", user.getId());
            response.put("nombre", user.getNombre());
            response.put("email", user.getEmail());
            response.put("rol", user.getRol());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Credenciales inválidas ❌");
            return ResponseEntity.status(401).body(response);
        }
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
