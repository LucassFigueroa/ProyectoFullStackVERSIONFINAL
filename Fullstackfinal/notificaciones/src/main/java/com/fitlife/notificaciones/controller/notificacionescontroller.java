package com.fitlife.notificaciones.controller;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.service.notificacionesservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notificaciones")
public class notificacionescontroller {

    @Autowired
    private final notificacionesservice notificacionesService;

    public notificacionescontroller(notificacionesservice notificacionesService) {
        this.notificacionesService = notificacionesService;
    }

    // POST - Crear notificacion
    @PostMapping
    public ResponseEntity<?> crearNotificacion(@Valid @RequestBody notificacionesmodel notificacion, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        notificacionesmodel creada = notificacionesService.crearNotificacion(notificacion);
        return ResponseEntity.ok(creada);
    }

    // Listar todas
    @GetMapping
    public List<notificacionesmodel> listarNotificaciones() {
        return notificacionesService.listarNotificaciones();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<notificacionesmodel> notificacion = notificacionesService.buscarPorId(id);
        if (notificacion.isPresent()) {
            return ResponseEntity.ok(notificacion.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Notificación no encontrada"));
        }
    }

    // Actualizar notificacion
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarNotificacion(@PathVariable Long id,
                                                    @Valid @RequestBody notificacionesmodel notificacion,
                                                    BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(notificacionesService.actualizarNotificacion(id, notificacion));
    }

    // Eliminar notificacion
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Long id) {
        notificacionesService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    // Filtrar por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<notificacionesmodel> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return notificacionesService.buscarPorUsuarioId(usuarioId);
    }

    // Filtrar por estado
    @GetMapping("/estado")
    public List<notificacionesmodel> buscarPorEstado(@RequestParam String estado) {
        return notificacionesService.buscarPorEstado(estado);
    }

    // Filtrar por usuario + estado
    @GetMapping("/usuarioEstado")
    public List<notificacionesmodel> buscarPorUsuarioYEstado(@RequestParam Long usuarioId,
                                                             @RequestParam String estado) {
        return notificacionesService.buscarPorUsuarioYEstado(usuarioId, estado);
    }

    // Manejador de JSON mal formado
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> manejarErroresJson(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Los datos enviados no tienen el formato correcto o están mal formateados."
        ));
    }
}