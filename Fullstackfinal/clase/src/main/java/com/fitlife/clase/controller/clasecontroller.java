package com.fitlife.clase.controller;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.service.claseservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clases")
@Tag(name = "Controlador de Clases", description = "Operaciones relacionadas con las clases de FitLife SPA")
public class clasecontroller {

    @Autowired
    private claseservice claseservice;

    @Operation(summary = "Crear una nueva clase")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Clase creada exitosamente")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public ResponseEntity<EntityModel<clase>> guardarClase(@RequestBody clase clase) {
        clase creada = claseservice.guardarClase(clase);
        return ResponseEntity
                .status(201)
                .body(EntityModel.of(creada,
                        linkTo(methodOn(clasecontroller.class).obtenerClasePorId(creada.getId())).withSelfRel(),
                        linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases")));
    }

    @Operation(summary = "Obtener todas las clases")
    @ApiResponse(responseCode = "200", description = "Clases encontradas")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<clase>>> obtenerTodasLasClases() {
        List<EntityModel<clase>> clases = claseservice.obtenerTodasLasClases().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(clasecontroller.class).obtenerClasePorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases")
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(clases,
                linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withSelfRel()));
    }

    @Operation(summary = "Obtener una clase por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clase encontrada"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<clase>> obtenerClasePorId(@PathVariable Long id) {
        clase encontrada = claseservice.obtenerClasePorId(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        return ResponseEntity.ok(EntityModel.of(encontrada,
                linkTo(methodOn(clasecontroller.class).obtenerClasePorId(id)).withSelfRel(),
                linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases")));
    }

    @Operation(summary = "Actualizar una clase por su ID")
    @ApiResponse(responseCode = "200", description = "Clase actualizada exitosamente")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<clase>> actualizarClase(@PathVariable Long id, @RequestBody clase claseDetails) {
        clase actualizada = claseservice.actualizarClase(id, claseDetails);
        return ResponseEntity.ok(EntityModel.of(actualizada,
                linkTo(methodOn(clasecontroller.class).obtenerClasePorId(id)).withSelfRel()));
    }

    @Operation(summary = "Eliminar una clase por su ID")
    @ApiResponse(responseCode = "204", description = "Clase eliminada exitosamente")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarClase(@PathVariable Long id) {
        claseservice.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }
}
