package com.fitlife.entrenador.controller;

import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.service.entrenadorservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entrenadores")
public class entrenadorcontroller {

    @Autowired
    private entrenadorservice entrenadorservice;

    // ✅ Solo ADMIN y STAFF pueden crear entrenadores
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public ResponseEntity<EntityModel<entrenador>> createEntrenador(@RequestBody entrenador entrenador) {
        entrenador saved = entrenadorservice.saveEntrenador(entrenador);
        EntityModel<entrenador> resource = EntityModel.of(saved,
                linkTo(methodOn(entrenadorcontroller.class).getEntrenadorById(saved.getId())).withSelfRel(),
                linkTo(methodOn(entrenadorcontroller.class).getAllEntrenadores()).withRel("all-entrenadores"));
        return ResponseEntity.ok(resource);
    }

    // ✅ ADMIN, STAFF y ENTRENADOR pueden ver lista
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ENTRENADOR')")
    @GetMapping
    public CollectionModel<EntityModel<entrenador>> getAllEntrenadores() {
        List<EntityModel<entrenador>> entrenadores = entrenadorservice.getAllEntrenadores().stream()
                .map(entrenador -> EntityModel.of(entrenador,
                        linkTo(methodOn(entrenadorcontroller.class).getEntrenadorById(entrenador.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(entrenadores,
                linkTo(methodOn(entrenadorcontroller.class).getAllEntrenadores()).withSelfRel());
    }

    // ✅ ADMIN, STAFF y ENTRENADOR pueden ver uno
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'ENTRENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<entrenador>> getEntrenadorById(@PathVariable Long id) {
        entrenador entrenador = entrenadorservice.getEntrenadorById(id);
        if (entrenador != null) {
            EntityModel<entrenador> resource = EntityModel.of(entrenador,
                    linkTo(methodOn(entrenadorcontroller.class).getEntrenadorById(id)).withSelfRel(),
                    linkTo(methodOn(entrenadorcontroller.class).getAllEntrenadores()).withRel("all-entrenadores"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Solo ADMIN y STAFF pueden actualizar
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<entrenador> updateEntrenador(@PathVariable Long id, @RequestBody entrenador entrenador) {
        entrenador updated = entrenadorservice.updateEntrenador(id, entrenador);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ Solo ADMIN puede eliminar
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrenador(@PathVariable Long id) {
        boolean deleted = entrenadorservice.deleteEntrenador(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
