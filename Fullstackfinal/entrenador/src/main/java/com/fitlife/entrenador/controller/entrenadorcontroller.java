package com.fitlife.entrenador.controller;

import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.service.entrenadorservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class entrenadorcontroller {

    @Autowired
    private entrenadorservice entrenadorservice;

    @PostMapping("/entrenadores")
    public entrenador create(@Valid @RequestBody entrenador entrenador) {
        return entrenadorservice.crearEntrenador(entrenador);
    }

    @GetMapping("/entrenadores")
    public List<entrenador> getAll() {
        return entrenadorservice.obtenerTodos();
    }

    @GetMapping("/entrenadores/{id}")
    public ResponseEntity<entrenador> getById(@PathVariable Long id) {
        return entrenadorservice.obtenerEntrenadorPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/entrenadores/{id}")
    public ResponseEntity<entrenador> update(@PathVariable Long id, @Valid @RequestBody entrenador details) {
        entrenador updated = entrenadorservice.actualizarEntrenador(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/entrenadores/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = entrenadorservice.eliminarEntrenador(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
