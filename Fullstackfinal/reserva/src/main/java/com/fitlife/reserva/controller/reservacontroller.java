package com.fitlife.reserva.controller;

import com.fitlife.reserva.model.reservamodel;
import com.fitlife.reserva.service.reservaservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Reservas", description = "Operaciones relacionadas con reservas")
@RestController
@RequestMapping("/api/reservas")
public class reservacontroller {

    @Autowired
    private reservaservice reservaservice;

    @Operation(summary = "Crear una nueva reserva")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody reservamodel reserva) {
        try {
            return ResponseEntity.ok(reservaservice.save(reserva));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Listar todas las reservas")
    @GetMapping
    public List<reservamodel> getAll() {
        return reservaservice.getAll();
    }

    @Operation(summary = "Obtener una reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<reservamodel> getById(@PathVariable Long id) {
        reservamodel reserva = reservaservice.getById(id);
        return reserva != null ? ResponseEntity.ok(reserva) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar una reserva")
    @PutMapping("/{id}")
    public ResponseEntity<reservamodel> update(@PathVariable Long id, @Valid @RequestBody reservamodel details) {
        reservamodel updated = reservaservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = reservaservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener reservas por ID de usuario")
    @GetMapping("/usuario/{usuarioId}")
    public List<reservamodel> getByUsuarioId(@PathVariable Long usuarioId) {
        return reservaservice.getByUsuarioId(usuarioId);
    }

    @Operation(summary = "Obtener reservas por fecha")
    @GetMapping("/fecha")
    public List<reservamodel> getByFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return reservaservice.getByFecha(fecha);
    }

    @Operation(summary = "Obtener reservas por rango de fechas")
    @GetMapping("/rango")
    public List<reservamodel> getByFechaBetween(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return reservaservice.getByFechaBetween(desde, hasta);
    }
}
