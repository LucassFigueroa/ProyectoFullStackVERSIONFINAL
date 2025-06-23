package com.fitlife.horario.controller;

import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.service.horarioservice;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horarios")
public class horariocontroller {

    @Autowired
    private horarioservice horarioService;

    // GET ALL con HATEOAS
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'STAFF', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<horariomodel>>> getAllHorarios() {
        List<EntityModel<horariomodel>> horarios = horarioService.getAllHorarios().stream()
                .map(horario -> EntityModel.of(horario,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(horariocontroller.class).getHorarioById(horario.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(horariocontroller.class).getAllHorarios()).withRel("horarios")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(horarios,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(horariocontroller.class).getAllHorarios()).withSelfRel()));
    }

    // GET BY ID con HATEOAS
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'STAFF', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<horariomodel>> getHorarioById(@PathVariable Long id) {
        Optional<horariomodel> horarioOpt = horarioService.getHorarioById(id);
        if (horarioOpt.isPresent()) {
            horariomodel horario = horarioOpt.get();
            EntityModel<horariomodel> horarioModel = EntityModel.of(horario,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(horariocontroller.class).getHorarioById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(horariocontroller.class).getAllHorarios()).withRel("horarios"));
            return ResponseEntity.ok(horarioModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST con validación y mensajes
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @PostMapping
    public ResponseEntity<?> crearHorario(@Valid @RequestBody horariomodel horario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        if (horario.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No debes incluir el ID al crear un horario."));
        }
        return new ResponseEntity<>(horarioService.crearHorario(horario), HttpStatus.CREATED);
    }

    // PUT con validación y mensajes
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarHorario(@PathVariable Long id, @Valid @RequestBody horariomodel horario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        horario.setId(id);
        return ResponseEntity.ok(horarioService.actualizarHorario(id, horario));
    }

    // DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar por fecha de inicio
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'STAFF', 'CLIENTE')")
    @GetMapping("/buscarPorInicio")
    public List<horariomodel> buscarPorFechaInicio(@RequestParam String inicio) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        return horarioService.buscarPorFechaInicio(fechaInicio);
    }

    // Buscar por fecha de fin
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'STAFF', 'CLIENTE')")
    @GetMapping("/buscarPorFin")
    public List<horariomodel> buscarPorFechaFin(@RequestParam String fin) {
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return horarioService.buscarPorFechaFin(fechaFin);
    }

    // Buscar por rango
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'STAFF', 'CLIENTE')")
    @GetMapping("/buscarRango")
    public List<horariomodel> buscarPorRango(@RequestParam String desde, @RequestParam String hasta) {
        LocalDateTime d1 = LocalDateTime.parse(desde);
        LocalDateTime d2 = LocalDateTime.parse(hasta);
        return horarioService.buscarPorRango(d1, d2);
    }

    // Buscar por entrenadorId
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'STAFF', 'CLIENTE')")
    @GetMapping("/buscarPorEntrenador")
    public List<horariomodel> buscarPorEntrenadorId(@RequestParam Long entrenadorId) {
        return horarioService.buscarPorEntrenadorId(entrenadorId);
    }
}
