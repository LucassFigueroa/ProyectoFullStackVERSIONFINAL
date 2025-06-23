package com.fitlife.reserva.controller;

import com.fitlife.reserva.model.reservamodel;
import com.fitlife.reserva.service.reservaservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Reservas", description = "Operaciones relacionadas con reservas")
@RestController
@RequestMapping("/api/reservas")
public class reservacontroller {

    @Autowired
    private reservaservice reservaservice;

    @PostMapping
    @Operation(summary = "Crear una nueva reserva")
    public ResponseEntity<?> create(@Valid @RequestBody reservamodel reserva) {
        try {
            reservamodel saved = reservaservice.save(reserva);
            return ResponseEntity.ok(toModel(saved));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas")
    public CollectionModel<EntityModel<reservamodel>> getAll() {
        List<reservamodel> reservas = reservaservice.getAll();

        List<EntityModel<reservamodel>> reservasConLinks = reservas.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(reservasConLinks,
            linkTo(methodOn(reservacontroller.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reserva por ID")
    public ResponseEntity<EntityModel<reservamodel>> getById(@PathVariable Long id) {
        reservamodel reserva = reservaservice.getById(id);
        return reserva != null
            ? ResponseEntity.ok(toModel(reserva))
            : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva")
    public ResponseEntity<EntityModel<reservamodel>> update(@PathVariable Long id, @Valid @RequestBody reservamodel details) {
        reservamodel updated = reservaservice.update(id, details);
        return updated != null
            ? ResponseEntity.ok(toModel(updated))
            : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return reservaservice.delete(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener reservas por ID de usuario")
    public CollectionModel<EntityModel<reservamodel>> getByUsuarioId(@PathVariable Long usuarioId) {
        List<EntityModel<reservamodel>> reservas = reservaservice.getByUsuarioId(usuarioId)
            .stream().map(this::toModel).toList();

        return CollectionModel.of(reservas,
            linkTo(methodOn(reservacontroller.class).getByUsuarioId(usuarioId)).withSelfRel());
    }

    @GetMapping("/fecha")
    @Operation(summary = "Obtener reservas por fecha")
    public CollectionModel<EntityModel<reservamodel>> getByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<EntityModel<reservamodel>> reservas = reservaservice.getByFecha(fecha)
            .stream().map(this::toModel).toList();

        return CollectionModel.of(reservas,
            linkTo(methodOn(reservacontroller.class).getByFecha(fecha)).withSelfRel());
    }

    @GetMapping("/rango")
    @Operation(summary = "Obtener reservas por rango de fechas")
    public CollectionModel<EntityModel<reservamodel>> getByFechaBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        List<EntityModel<reservamodel>> reservas = reservaservice.getByFechaBetween(desde, hasta)
            .stream().map(this::toModel).toList();

        return CollectionModel.of(reservas,
            linkTo(methodOn(reservacontroller.class).getByFechaBetween(desde, hasta)).withSelfRel());
    }

    private EntityModel<reservamodel> toModel(reservamodel reserva) {
        return EntityModel.of(reserva,
            linkTo(methodOn(reservacontroller.class).getById(reserva.getId())).withSelfRel(),
            linkTo(methodOn(reservacontroller.class).getAll()).withRel("reservas"),
            linkTo(methodOn(reservacontroller.class).getByUsuarioId(reserva.getUsuarioId())).withRel("usuario"),
            linkTo(methodOn(reservacontroller.class).getByFecha(reserva.getFecha())).withRel("fecha")
        );
    }
}
