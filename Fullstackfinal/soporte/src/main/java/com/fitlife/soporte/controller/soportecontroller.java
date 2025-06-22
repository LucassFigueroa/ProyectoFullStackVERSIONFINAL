package com.fitlife.soporte.controller;

import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.service.soporteservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class soportecontroller {

    @Autowired
    private soporteservice soporteservice;

    @PostMapping("/soportes")
    public ResponseEntity<soportemodel> create(@Valid @RequestBody soportemodel soporte) {
        return ResponseEntity.ok(soporteservice.save(soporte));
    }

    @GetMapping("/soportes")
    public List<soportemodel> getAll() {
        return soporteservice.getAll();
    }

    @GetMapping("/soportes/{id}")
    public ResponseEntity<soportemodel> getById(@PathVariable Long id) {
        soportemodel soporte = soporteservice.getById(id);
        return soporte != null ? ResponseEntity.ok(soporte) : ResponseEntity.notFound().build();
    }

    @PutMapping("/soportes/{id}")
    public ResponseEntity<soportemodel> update(@PathVariable Long id, @Valid @RequestBody soportemodel details) {
        soportemodel updated = soporteservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/soportes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = soporteservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/soportes/search")
    public List<soportemodel> searchByAsunto(@RequestParam String keyword) {
        return soporteservice.findByAsunto(keyword);
    }
}
