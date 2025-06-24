package com.fitlife.notificaciones.controller;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.service.notificacionesservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificaciones")
public class notificacionescontroller {

    @Autowired
    private notificacionesservice notificacionesService;

    @PostMapping
    public ResponseEntity<?> crearNotificacion(@Valid @RequestBody notificacionesmodel notif, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        if (notif.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No envíes ID al crear"));
        }
        return ResponseEntity.ok(notificacionesService.crearNotificacion(notif));
    }

    @GetMapping
    public CollectionModel<EntityModel<notificacionesmodel>> listarNotificaciones() {
        List<EntityModel<notificacionesmodel>> lista = notificacionesService.listarNotificaciones().stream()
                .map(notif -> EntityModel.of(notif,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(notificacionescontroller.class)
                                .obtenerNotificacion(notif.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(lista,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(notificacionescontroller.class)
                        .listarNotificaciones()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerNotificacion(@PathVariable Long id) {
        Optional<notificacionesmodel> notif = notificacionesService.obtenerPorId(id);
        if (notif.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "No encontrada"));
        }
        EntityModel<notificacionesmodel> resource = EntityModel.of(notif.get(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(notificacionescontroller.class)
                        .obtenerNotificacion(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarNotificacion(
            @PathVariable Long id,
            @Valid @RequestBody notificacionesmodel notif,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(notificacionesService.actualizarNotificacion(id, notif));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Long id) {
        notificacionesService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<notificacionesmodel> getByUsuario(@PathVariable Long usuarioId) {
        return notificacionesService.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/estado")
    public List<notificacionesmodel> getByEstado(@RequestParam String estado) {
        return notificacionesService.buscarPorEstado(estado);
    }

    @GetMapping("/usuario/{usuarioId}/estado")
    public List<notificacionesmodel> getByUsuarioAndEstado(
            @PathVariable Long usuarioId,
            @RequestParam String estado) {
        return notificacionesService.buscarPorUsuarioYEstado(usuarioId, estado);
    }

}
