package com.fitlife.soporte.controller;

import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.service.soporteservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class soportecontroller {

    @Autowired
    private soporteservice soporteservice;

    @PostMapping("/soportes")
    public ResponseEntity<EntityModel<soportemodel>> create(@Valid @RequestBody soportemodel soporte) {
        soportemodel saved = soporteservice.save(soporte);
        return ResponseEntity.ok(toModel(saved));
    }

    @GetMapping("/soportes")
    public CollectionModel<EntityModel<soportemodel>> getAll() {
        List<soportemodel> soportes = soporteservice.getAll();
        List<EntityModel<soportemodel>> modelos = soportes.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelos,
                linkTo(methodOn(soportecontroller.class).getAll()).withSelfRel());
    }

    @GetMapping("/soportes/{id}")
    public ResponseEntity<EntityModel<soportemodel>> getById(@PathVariable Long id) {
        soportemodel soporte = soporteservice.getById(id);
        return soporte != null
                ? ResponseEntity.ok(toModel(soporte))
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/soportes/{id}")
    public ResponseEntity<EntityModel<soportemodel>> update(@PathVariable Long id, @Valid @RequestBody soportemodel details) {
        soportemodel updated = soporteservice.update(id, details);
        return updated != null
                ? ResponseEntity.ok(toModel(updated))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/soportes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = soporteservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/soportes/search")
    public CollectionModel<EntityModel<soportemodel>> searchByAsunto(@RequestParam String keyword) {
        List<soportemodel> resultados = soporteservice.findByAsunto(keyword);
        List<EntityModel<soportemodel>> modelos = resultados.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelos,
                linkTo(methodOn(soportecontroller.class).searchByAsunto(keyword)).withSelfRel());
    }

    // Método que agrega los enlaces HATEOAS directamente
    private EntityModel<soportemodel> toModel(soportemodel soporte) {
        return EntityModel.of(soporte,
                linkTo(methodOn(soportecontroller.class).getById(soporte.getId())).withSelfRel(),
                linkTo(methodOn(soportecontroller.class).getAll()).withRel("todos"),
                linkTo(methodOn(soportecontroller.class).searchByAsunto(soporte.getAsunto())).withRel("buscar-por-asunto"));
    }
}
