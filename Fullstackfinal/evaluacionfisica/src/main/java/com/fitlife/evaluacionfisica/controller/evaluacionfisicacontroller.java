package com.fitlife.evaluacionfisica.controller;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.service.evaluacionfisicaservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class evaluacionfisicacontroller {

    @Autowired
    private evaluacionfisicaservice evaluacionfisicaservice;

    @PostMapping("/evaluaciones")
    public evaluacionfisica create(@Valid @RequestBody evaluacionfisica evaluacion) {
        return evaluacionfisicaservice.save(evaluacion);
    }

    @GetMapping("/evaluaciones")
    public List<evaluacionfisica> getAll() {
        return evaluacionfisicaservice.getAll();
    }

    @GetMapping("/evaluaciones/{id}")
    public ResponseEntity<evaluacionfisica> getById(@PathVariable Long id) {
        evaluacionfisica evaluacion = evaluacionfisicaservice.getById(id);
        return evaluacion != null ? ResponseEntity.ok(evaluacion) : ResponseEntity.notFound().build();
    }

    @PutMapping("/evaluaciones/{id}")
    public ResponseEntity<evaluacionfisica> update(@PathVariable Long id, @Valid @RequestBody evaluacionfisica details) {
        evaluacionfisica updated = evaluacionfisicaservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/evaluaciones/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = evaluacionfisicaservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
