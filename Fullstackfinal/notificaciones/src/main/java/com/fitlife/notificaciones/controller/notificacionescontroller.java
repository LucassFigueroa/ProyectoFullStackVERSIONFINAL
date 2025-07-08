package com.fitlife.notificaciones.controller;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.service.notificacionesservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Controlador de Notificaciones", description = "CRUD y búsqueda de notificaciones de usuarios en FitLife SPA")
public class notificacionescontroller {

    @Autowired
    private notificacionesservice notificacionesService;

    @Operation(summary = "Crear una nueva notificación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o ID enviado indebidamente")
    })
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

    @Operation(summary = "Listar todas las notificaciones")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
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

    @Operation(summary = "Obtener notificación por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontró la notificación con ese ID")
    })
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

    @Operation(summary = "Actualizar una notificación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
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

    @Operation(summary = "Eliminar una notificación")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Long id) {
        notificacionesService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar notificaciones por usuario ID")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public List<notificacionesmodel> getByUsuario(@PathVariable Long usuarioId) {
        return notificacionesService.buscarPorUsuario(usuarioId);
    }

    @Operation(summary = "Buscar notificaciones por estado")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por estado")
    @GetMapping("/estado")
    public List<notificacionesmodel> getByEstado(@RequestParam String estado) {
        return notificacionesService.buscarPorEstado(estado);
    }

    @Operation(summary = "Buscar notificaciones por usuario y estado")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por usuario y estado")
    @GetMapping("/usuario/{usuarioId}/estado")
    public List<notificacionesmodel> getByUsuarioAndEstado(
            @PathVariable Long usuarioId,
            @RequestParam String estado) {
        return notificacionesService.buscarPorUsuarioYEstado(usuarioId, estado);
    }
}
