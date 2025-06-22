package com.fitlife.evaluacionfisica.controller;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.service.evaluacionfisicaservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Evaluaciones físicas", description = "CRUD y filtros para evaluaciones físicas")
@RestController
@RequestMapping("/api")
public class evaluacionfisicacontroller {

    @Autowired
    private evaluacionfisicaservice evaluacionfisicaservice;

    @Operation(summary = "Crear nueva evaluación")
    @PostMapping("/evaluaciones")
    public evaluacionfisica create(@Valid @RequestBody evaluacionfisica evaluacion) {
        return evaluacionfisicaservice.save(evaluacion);
    }

    @Operation(summary = "Obtener todas las evaluaciones")
    @GetMapping("/evaluaciones")
    public List<evaluacionfisica> getAll() {
        return evaluacionfisicaservice.getAll();
    }

    @Operation(summary = "Obtener evaluación por ID")
    @GetMapping("/evaluaciones/{id}")
    public ResponseEntity<evaluacionfisica> getById(@PathVariable Long id) {
        evaluacionfisica evaluacion = evaluacionfisicaservice.getById(id);
        return evaluacion != null ? ResponseEntity.ok(evaluacion) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar evaluación por ID")
    @PutMapping("/evaluaciones/{id}")
    public ResponseEntity<evaluacionfisica> update(@PathVariable Long id, @Valid @RequestBody evaluacionfisica details) {
        evaluacionfisica updated = evaluacionfisicaservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar evaluación por ID")
    @DeleteMapping("/evaluaciones/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = evaluacionfisicaservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Filtrar por usuario")
    @GetMapping("/evaluaciones/usuario/{usuarioid}")
    public List<evaluacionfisica> getByUsuarioId(@PathVariable Long usuarioid) {
        return evaluacionfisicaservice.getByUsuarioId(usuarioid);
    }

    @Operation(summary = "Filtrar por rango de fechas")
    @GetMapping("/evaluaciones/filtro/fechas")
    public List<evaluacionfisica> getByFechaEvaluacionBetween(
            @RequestParam String desde,
            @RequestParam String hasta) {
        LocalDate fechaDesde = LocalDate.parse(desde);
        LocalDate fechaHasta = LocalDate.parse(hasta);
        return evaluacionfisicaservice.getByFechaEvaluacionBetween(fechaDesde, fechaHasta);
    }
}
