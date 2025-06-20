package com.fitlife.inventario.controller;

import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.service.inventarioservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
public class inventariocontroller {

     @Autowired
    private inventarioservice inventarioService;

    @PostMapping
    public ResponseEntity<?> crearInventario(@Valid @RequestBody inventariomodel inventario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }

        if (inventario.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No debes incluir el ID al crear un inventario."));
        }

        return ResponseEntity.ok(inventarioService.crearInventario(inventario));
    }

    @GetMapping
    public List<inventariomodel> listarInventarios() {
        return inventarioService.listarInventarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInventario(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            inventariomodel inventario = inventarioService.obtenerInventarioPorId(idLong);
            if (inventario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Inventario no encontrado"));
            }
            return ResponseEntity.ok(inventario);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "El ID proporcionado no es válido"));
        }
    }

    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
    public ResponseEntity<?> manejarErroresDeDeserializacion(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Los datos enviados son incorrectos o están mal formateados."
        ));
    }
}
