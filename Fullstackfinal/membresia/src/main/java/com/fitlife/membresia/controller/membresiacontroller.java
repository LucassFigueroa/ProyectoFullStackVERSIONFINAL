package com.fitlife.membresia.controller;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.service.membresiaservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/membresias")
public class membresiacontroller {

    @Autowired
    private membresiaservice membresiaservice;

    @PostMapping
    public ResponseEntity<EntityModel<membresiamodel>> createMembresia(@RequestBody membresiamodel membresia) {
        membresiamodel saved = membresiaservice.saveMembresia(membresia);
        EntityModel<membresiamodel> resource = EntityModel.of(saved,
                linkTo(methodOn(membresiacontroller.class).getMembresiaById(saved.getId())).withSelfRel(),
                linkTo(methodOn(membresiacontroller.class).getAllMembresias()).withRel("all-membresias"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public CollectionModel<EntityModel<membresiamodel>> getAllMembresias() {
        List<EntityModel<membresiamodel>> membresias = membresiaservice.getAllMembresias().stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(membresiacontroller.class).getMembresiaById(m.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(membresias,
                linkTo(methodOn(membresiacontroller.class).getAllMembresias()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<membresiamodel>> getMembresiaById(@PathVariable Long id) {
        membresiamodel membresia = membresiaservice.getMembresiaById(id);
        if (membresia != null) {
            EntityModel<membresiamodel> resource = EntityModel.of(membresia,
                    linkTo(methodOn(membresiacontroller.class).getMembresiaById(id)).withSelfRel(),
                    linkTo(methodOn(membresiacontroller.class).getAllMembresias()).withRel("all-membresias"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<membresiamodel> updateMembresia(@PathVariable Long id, @RequestBody membresiamodel membresia) {
        membresiamodel updated = membresiaservice.updateMembresia(id, membresia);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembresia(@PathVariable Long id) {
        boolean deleted = membresiaservice.deleteMembresia(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
