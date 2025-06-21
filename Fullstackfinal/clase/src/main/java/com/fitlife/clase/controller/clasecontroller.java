package com.fitlife.clase.controller;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.service.claseservice;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class clasecontroller {

    @Autowired
    private claseservice claseService;

    @PostMapping("/clases")
    public clase createClase(@Valid @RequestBody clase clase) {
        return claseService.guardarClase(clase);
    }

    @GetMapping("/clases")
    public List<clase> getAllClases() {
        return claseService.obtenerTodasLasClases();
    }

    @GetMapping("/clases/{id}")
    public ResponseEntity<clase> getClaseById(@PathVariable Long id) {
        Optional<clase> clase = claseService.obtenerClasePorId(id);
        return clase.isPresent() ? ResponseEntity.ok(clase.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/clases/{id}")
    public ResponseEntity<clase> updateClase(@PathVariable Long id, @Valid @RequestBody clase claseDetails) {
        clase updated = claseService.actualizarClase(id, claseDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clases/{id}")
    public ResponseEntity<Void> deleteClase(@PathVariable Long id) {
        try {
            claseService.eliminarClase(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
