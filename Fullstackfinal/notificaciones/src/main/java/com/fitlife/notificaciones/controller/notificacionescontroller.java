package com.fitlife.notificaciones.controller;

import com.fitlife.notificaciones.model.notificacionesmodel;
import com.fitlife.notificaciones.service.notificacionesservice;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificaciones")
public class notificacionescontroller {
     
    @Autowired
    private final notificacionesservice notificacionesService;

    public notificacionescontroller(notificacionesservice notificacionesService) {
        this.notificacionesService = notificacionesService;
    }

    @PostMapping
    public ResponseEntity<?> crearNotificacion(@Valid @RequestBody notificacionesmodel notificacion, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        notificacionesmodel creada = notificacionesService.crearNotificacion(notificacion);
        return ResponseEntity.ok(creada);
    }

    @GetMapping
    public List<notificacionesmodel> listarNotificaciones() {
        return notificacionesService.listarNotificaciones();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<notificacionesmodel> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return notificacionesService.buscarPorUsuarioId(usuarioId);
    }
    
}
