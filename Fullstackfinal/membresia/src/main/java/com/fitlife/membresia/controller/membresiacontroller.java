package com.fitlife.membresia.controller;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.service.membresiaservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/membresias")
public class membresiacontroller {

    @Autowired
    private membresiaservice membresiaService;

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

    @GetMapping
    public List<membresiamodel> obtener() {
        return membresiaService.obtenerTodas();
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> manejarErroresJson(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Los datos enviados no tienen el formato correcto o están mal formateados."
        ));
    }
}
