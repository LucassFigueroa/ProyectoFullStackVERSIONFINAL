package com.fitlife.membresia.controller;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.service.membresiaservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Controlador de Membresías", description = "CRUD y búsqueda de membresías del sistema FitLife SPA")
public class membresiacontroller {

    @Autowired
    private membresiaservice membresiaservice;

    @Operation(summary = "Crear una nueva membresía")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Membresía creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    @PostMapping
    public ResponseEntity<EntityModel<membresiamodel>> createMembresia(@RequestBody membresiamodel membresia) {
        membresiamodel saved = membresiaservice.saveMembresia(membresia);
        EntityModel<membresiamodel> resource = EntityModel.of(saved,
                linkTo(methodOn(membresiacontroller.class).getMembresiaById(saved.getId())).withSelfRel(),
                linkTo(methodOn(membresiacontroller.class).getAllMembresias()).withRel("all-membresias"));
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Listar todas las membresías")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de membresías obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No se encontraron membresías registradas")
    })
    @GetMapping
    public CollectionModel<EntityModel<membresiamodel>> getAllMembresias() {
        List<EntityModel<membresiamodel>> membresias = membresiaservice.getAllMembresias().stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(membresiacontroller.class).getMembresiaById(m.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(membresias,
                linkTo(methodOn(membresiacontroller.class).getAllMembresias()).withSelfRel());
    }

    @Operation(summary = "Obtener una membresía por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Membresía encontrada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró una membresía con ese ID")
    })
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

    @Operation(summary = "Actualizar una membresía existente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Membresía actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró una membresía con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<membresiamodel> updateMembresia(@PathVariable Long id, @RequestBody membresiamodel membresia) {
        membresiamodel updated = membresiaservice.updateMembresia(id, membresia);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una membresía por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Membresía eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró una membresía con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembresia(@PathVariable Long id) {
        boolean deleted = membresiaservice.deleteMembresia(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
