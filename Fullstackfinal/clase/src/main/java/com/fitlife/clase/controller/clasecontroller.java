package com.fitlife.clase.controller;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.service.claseservice;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class clasecontroller {

    @Autowired
    private claseservice claseService;

    @PostMapping("/clases")
    public clase createClase(@Valid @RequestBody clase clase) {
        return claseService.saveClase(clase);
    }

    @GetMapping("/clases")
    public List<clase> getAllClases() {
        return claseService.getAllClases();
    }

    @GetMapping("/clases/{id}")
    public ResponseEntity<clase> getClaseById(@PathVariable Long id) {
        clase clase = claseService.getClaseById(id);
        return clase != null ? ResponseEntity.ok(clase) : ResponseEntity.notFound().build();
    }

    @PutMapping("/clases/{id}")
    public ResponseEntity<clase> updateClase(@PathVariable Long id, @Valid @RequestBody clase claseDetails) {
        clase updated = claseService.updateClase(id, claseDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clases/{id}")
    public ResponseEntity<Void> deleteClase(@PathVariable Long id) {
        boolean deleted = claseService.deleteClase(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
