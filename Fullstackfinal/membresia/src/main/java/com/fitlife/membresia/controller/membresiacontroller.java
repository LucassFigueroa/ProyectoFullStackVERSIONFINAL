package com.fitlife.membresia.controller;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.service.membresiaservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/membresias")
public class membresiacontroller {

    @Autowired
    private membresiaservice membresiaService;

    // Crear nueva membresia
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody membresiamodel membresia, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }

        if (membresia.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No debes incluir el ID al crear una membresía."));
        }

        return ResponseEntity.ok(membresiaService.guardarMembresia(membresia));
    }

    // Listar todas
    @GetMapping
    public List<membresiamodel> obtener() {
        return membresiaService.obtenerTodas();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<membresiamodel> membresia = membresiaService.obtenerPorId(id);
        if (membresia.isPresent()) {
            return ResponseEntity.ok(membresia.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Membresía no encontrada"));
        }
    }

    // Actualizar membresia
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody membresiamodel membresia, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(membresiaService.actualizarMembresia(id, membresia));
    }

    // Eliminar o cancelar membresia
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        membresiaService.eliminarMembresia(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar por usuario
    @GetMapping("/buscarPorUsuario")
    public List<membresiamodel> buscarPorUsuario(@RequestParam Long usuarioId) {
        return membresiaService.buscarPorUsuario(usuarioId);
    }

    // Buscar por estado
    @GetMapping("/buscarPorEstado")
    public List<membresiamodel> buscarPorEstado(@RequestParam String estado) {
        return membresiaService.buscarPorEstado(estado);
    }

    // Buscar por usuario + estado
    @GetMapping("/buscarPorUsuarioYEstado")
    public List<membresiamodel> buscarPorUsuarioYEstado(
            @RequestParam Long usuarioId,
            @RequestParam String estado) {
        return membresiaService.buscarPorUsuarioYEstado(usuarioId, estado);
    }

    // Manejar JSON mal formados
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> manejarErroresJson(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Los datos enviados no tienen el formato correcto o están mal formateados."
        ));
    }
}