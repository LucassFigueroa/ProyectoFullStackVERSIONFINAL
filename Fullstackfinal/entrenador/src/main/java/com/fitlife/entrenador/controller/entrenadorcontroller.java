package com.fitlife.entrenador.controller;

import com.fitlife.entrenador.model.entrenador;
import com.fitlife.entrenador.service.entrenadorservice;
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
@RequestMapping("/api/entrenadores")
@Tag(name = "Controlador de Entrenadores", description = "Operaciones CRUD sobre entrenadores en FitLife SPA")
public class entrenadorcontroller {

    @Autowired
    private entrenadorservice entrenadorservice;

    @Operation(summary = "Crear un nuevo entrenador")
    @ApiResponse(responseCode = "200", description = "Entrenador creado exitosamente")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public ResponseEntity<EntityModel<entrenador>> createEntrenador(@RequestBody entrenador entrenador) {
        entrenador saved = entrenadorservice.saveEntrenador(entrenador);
        EntityModel<entrenador> resource = EntityModel.of(saved,
                linkTo(methodOn(entrenadorcontroller.class).getEntrenadorById(saved.getId())).withSelfRel(),
                linkTo(methodOn(entrenadorcontroller.class).getAllEntrenadores()).withRel("all-entrenadores"));
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Obtener todos los entrenadores")
    @ApiResponse(responseCode = "200", description = "Entrenadores encontrados")
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

    @Operation(summary = "Obtener un entrenador por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Entrenador encontrado"),
        @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
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

    @Operation(summary = "Actualizar un entrenador por ID")
    @ApiResponse(responseCode = "200", description = "Entrenador actualizado")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<entrenador> updateEntrenador(@PathVariable Long id, @RequestBody entrenador entrenador) {
        entrenador updated = entrenadorservice.updateEntrenador(id, entrenador);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un entrenador por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Entrenador eliminado"),
        @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrenador(@PathVariable Long id) {
        boolean deleted = entrenadorservice.deleteEntrenador(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
