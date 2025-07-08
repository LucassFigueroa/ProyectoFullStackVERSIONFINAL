package com.fitlife.reserva.controller;

import com.fitlife.reserva.model.reserva;
import com.fitlife.reserva.service.reservaservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operaciones relacionadas con reservas")
public class reservacontroller {

    private final reservaservice reservaservice;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Crear una nueva reserva")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reserva creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto de horario")
    })
    public ResponseEntity<?> createReserva(@RequestBody reserva reserva) {
        try {
            reserva saved = reservaservice.saveReserva(reserva);
            EntityModel<reserva> resource = EntityModel.of(saved,
                    linkTo(methodOn(reservacontroller.class).getReservaById(saved.getId())).withSelfRel(),
                    linkTo(methodOn(reservacontroller.class).getAllReservas()).withRel("all-reservas"));
            return ResponseEntity.ok(resource);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Obtener todas las reservas")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    public ResponseEntity<CollectionModel<EntityModel<reserva>>> getAllReservas() {
        List<EntityModel<reserva>> reservas = reservaservice.getAllReservas().stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(reservacontroller.class).getReservaById(r.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(reservas,
                linkTo(methodOn(reservacontroller.class).getAllReservas()).withSelfRel()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reserva por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<?> getReservaById(@PathVariable Long id) {
        Optional<reserva> r = reservaservice.getReservaById(id);
        return r.map(value -> ResponseEntity.ok(
                        EntityModel.of(value,
                                linkTo(methodOn(reservacontroller.class).getReservaById(id)).withSelfRel(),
                                linkTo(methodOn(reservacontroller.class).getAllReservas()).withRel("all-reservas"))
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody reserva reserva) {
        reserva updated = reservaservice.updateReserva(id, reserva);
        if (updated != null) {
            EntityModel<reserva> resource = EntityModel.of(updated,
                    linkTo(methodOn(reservacontroller.class).getReservaById(updated.getId())).withSelfRel(),
                    linkTo(methodOn(reservacontroller.class).getAllReservas()).withRel("all-reservas"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        if (reservaservice.deleteReserva(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cliente/{nombre}")
    @Operation(summary = "Buscar reservas por nombre del cliente")
    @ApiResponse(responseCode = "200", description = "Reservas filtradas por cliente encontradas correctamente")
    public ResponseEntity<CollectionModel<EntityModel<reserva>>> getReservasByClienteNombre(@PathVariable String nombre) {
        List<EntityModel<reserva>> reservas = reservaservice.getReservasByClienteNombre(nombre).stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(reservacontroller.class).getReservaById(r.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(reservas,
                linkTo(methodOn(reservacontroller.class).getAllReservas()).withSelfRel()));
    }
}
