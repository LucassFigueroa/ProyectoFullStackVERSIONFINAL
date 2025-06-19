package com.fitlife.reporte.controller;

import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.service.reporteservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class reportecontroller {

    @Autowired
    private reporteservice reporteservice;

    @PostMapping("/reportes")
    public reportemodel create(@Valid @RequestBody reportemodel reporte) {
        return reporteservice.save(reporte);
    }

    @GetMapping("/reportes")
    public List<reportemodel> getAll() {
        return reporteservice.getAll();
    }

    @GetMapping("/reportes/{id}")
    public ResponseEntity<reportemodel> getById(@PathVariable Long id) {
        reportemodel reporte = reporteservice.getById(id);
        return reporte != null ? ResponseEntity.ok(reporte) : ResponseEntity.notFound().build();
    }

    @PutMapping("/reportes/{id}")
    public ResponseEntity<reportemodel> update(@PathVariable Long id, @Valid @RequestBody reportemodel details) {
        reportemodel updated = reporteservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = reporteservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
