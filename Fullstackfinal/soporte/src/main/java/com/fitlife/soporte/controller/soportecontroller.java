package com.fitlife.soporte.controller;

import com.fitlife.soporte.model.soportemodel;
import com.fitlife.soporte.service.soporteservice;
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
@RequestMapping("/api/soporte")
@Tag(name = "Controlador de Soporte", description = "Operaciones CRUD del microservicio de Soporte")
public class soportecontroller {

    @Autowired
    private soporteservice soporteservice;

    @Operation(summary = "Crear una solicitud de soporte")
    @ApiResponse(responseCode = "200", description = "Soporte creado exitosamente")
    @PostMapping
    public ResponseEntity<EntityModel<soportemodel>> createSoporte(@RequestBody soportemodel soporte) {
        soportemodel saved = soporteservice.saveSoporte(soporte);
        EntityModel<soportemodel> resource = EntityModel.of(saved,
                linkTo(methodOn(soportecontroller.class).getSoporteById(saved.getId())).withSelfRel(),
                linkTo(methodOn(soportecontroller.class).getAllSoporte()).withRel("all-soporte"));
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Obtener todas las solicitudes de soporte")
    @ApiResponse(responseCode = "200", description = "Listado de solicitudes de soporte")
    @GetMapping
    public CollectionModel<EntityModel<soportemodel>> getAllSoporte() {
        List<EntityModel<soportemodel>> soporteList = soporteservice.getAllSoporte().stream()
                .map(s -> EntityModel.of(s,
                        linkTo(methodOn(soportecontroller.class).getSoporteById(s.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(soporteList,
                linkTo(methodOn(soportecontroller.class).getAllSoporte()).withSelfRel());
    }

    @Operation(summary = "Obtener una solicitud de soporte por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Soporte encontrado"),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<soportemodel>> getSoporteById(@PathVariable Long id) {
        soportemodel soporte = soporteservice.getSoporteById(id);
        if (soporte != null) {
            EntityModel<soportemodel> resource = EntityModel.of(soporte,
                    linkTo(methodOn(soportecontroller.class).getSoporteById(id)).withSelfRel(),
                    linkTo(methodOn(soportecontroller.class).getAllSoporte()).withRel("all-soporte"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar una solicitud de soporte por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Soporte actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<soportemodel> updateSoporte(@PathVariable Long id, @RequestBody soportemodel soporte) {
        soportemodel updated = soporteservice.updateSoporte(id, soporte);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una solicitud de soporte por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Soporte eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoporte(@PathVariable Long id) {
        boolean deleted = soporteservice.deleteSoporte(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
