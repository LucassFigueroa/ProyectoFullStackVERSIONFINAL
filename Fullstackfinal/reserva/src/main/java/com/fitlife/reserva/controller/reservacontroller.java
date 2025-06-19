package com.fitlife.reserva.controller;

import com.fitlife.reserva.model.reservamodel;
import com.fitlife.reserva.service.reservaservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class reservacontroller {

    @Autowired
    private reservaservice reservaservice;

    @PostMapping("/reservas")
    public reservamodel create(@Valid @RequestBody reservamodel reserva) {
        return reservaservice.save(reserva);
    }

    @GetMapping("/reservas")
    public List<reservamodel> getAll() {
        return reservaservice.getAll();
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<reservamodel> getById(@PathVariable Long id) {
        reservamodel reserva = reservaservice.getById(id);
        return reserva != null ? ResponseEntity.ok(reserva) : ResponseEntity.notFound().build();
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<reservamodel> update(@PathVariable Long id, @Valid @RequestBody reservamodel details) {
        reservamodel updated = reservaservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = reservaservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
