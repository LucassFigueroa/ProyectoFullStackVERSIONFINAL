package com.fitlife.acceso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fitlife.acceso.model.accesomodel;
import com.fitlife.acceso.service.accesoservice;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class accesocontroller {

    @Autowired
    private accesoservice accesoService;

    @PostMapping("/accesos")
    public accesomodel createAcceso(@Valid @RequestBody accesomodel acceso) {
        return accesoService.saveAcceso(acceso);
    }

    @GetMapping("/accesos")
    public List<accesomodel> getAllAccesos() {
        return accesoService.getAllAccesos();
    }

    @GetMapping("/accesos/{id}")
    public ResponseEntity<accesomodel> getAccesoById(@PathVariable Long id) {
        accesomodel acceso = accesoService.getAccesoById(id);
        return acceso != null ? ResponseEntity.ok(acceso) : ResponseEntity.notFound().build();
    }

    @PutMapping("/accesos/{id}")
    public ResponseEntity<accesomodel> updateAcceso(@PathVariable Long id, @Valid @RequestBody accesomodel accesoDetails) {
        accesomodel updatedAcceso = accesoService.updateAcceso(id, accesoDetails);
        return updatedAcceso != null ? ResponseEntity.ok(updatedAcceso) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/accesos/{id}")
    public ResponseEntity<Void> deleteAcceso(@PathVariable Long id) {
        boolean deleted = accesoService.deleteAcceso(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
