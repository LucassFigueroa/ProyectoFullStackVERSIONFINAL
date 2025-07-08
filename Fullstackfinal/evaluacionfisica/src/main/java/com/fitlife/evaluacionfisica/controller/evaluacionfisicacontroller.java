package com.fitlife.evaluacionfisica.controller;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.service.evaluacionfisicaservice;
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
@RequestMapping("/api/evaluaciones")
@Tag(name = "Controlador de Evaluaciones Físicas", description = "Operaciones relacionadas con evaluaciones físicas en FitLife SPA")
public class evaluacionfisicacontroller {

    @Autowired
    private evaluacionfisicaservice evaluacionfisicaservice;

    @Operation(summary = "Crear una nueva evaluación física")
    @ApiResponse(responseCode = "200", description = "Evaluación física creada exitosamente")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public ResponseEntity<EntityModel<evaluacionfisica>> createEvaluacion(@RequestBody evaluacionfisica evaluacion) {
        evaluacionfisica saved = evaluacionfisicaservice.saveEvaluacion(evaluacion);
        EntityModel<evaluacionfisica> resource = EntityModel.of(saved,
                linkTo(methodOn(evaluacionfisicacontroller.class).getEvaluacionById(saved.getId())).withSelfRel(),
                linkTo(methodOn(evaluacionfisicacontroller.class).getAllEvaluaciones()).withRel("all-evaluaciones"));
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Obtener todas las evaluaciones físicas")
    @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping
    public CollectionModel<EntityModel<evaluacionfisica>> getAllEvaluaciones() {
        List<EntityModel<evaluacionfisica>> evaluaciones = evaluacionfisicaservice.getAllEvaluaciones().stream()
                .map(ev -> EntityModel.of(ev,
                        linkTo(methodOn(evaluacionfisicacontroller.class).getEvaluacionById(ev.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(evaluaciones,
                linkTo(methodOn(evaluacionfisicacontroller.class).getAllEvaluaciones()).withSelfRel());
    }

    @Operation(summary = "Obtener una evaluación física por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluación encontrada"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<evaluacionfisica>> getEvaluacionById(@PathVariable Long id) {
        evaluacionfisica ev = evaluacionfisicaservice.getEvaluacionById(id);
        if (ev != null) {
            EntityModel<evaluacionfisica> resource = EntityModel.of(ev,
                    linkTo(methodOn(evaluacionfisicacontroller.class).getEvaluacionById(id)).withSelfRel(),
                    linkTo(methodOn(evaluacionfisicacontroller.class).getAllEvaluaciones()).withRel("all-evaluaciones"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar una evaluación física por ID")
    @ApiResponse(responseCode = "200", description = "Evaluación actualizada exitosamente")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<evaluacionfisica> updateEvaluacion(@PathVariable Long id, @RequestBody evaluacionfisica evaluacion) {
        evaluacionfisica updated = evaluacionfisicaservice.updateEvaluacion(id, evaluacion);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una evaluación física por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evaluación eliminada"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluacion(@PathVariable Long id) {
        boolean deleted = evaluacionfisicaservice.deleteEvaluacion(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
