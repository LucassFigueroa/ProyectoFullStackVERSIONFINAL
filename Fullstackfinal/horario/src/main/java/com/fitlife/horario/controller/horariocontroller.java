package com.fitlife.horario.controller;


import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.service.horarioservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.List;

@RestController
@RequestMapping("/horarios")
public class horariocontroller {

    private final horarioservice horarioService;

    public horariocontroller(horarioservice horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping
    public List<horariomodel> listarHorarios() {
        return horarioService.listarHorarios();
    }

    @PostMapping
    public ResponseEntity<?> guardarHorario(@Valid @RequestBody horariomodel horario, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errores = new StringBuilder();
            result.getFieldErrors().forEach(error -> {
                errores.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append(". ");
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores.toString().trim());
        }

        return ResponseEntity.ok(horarioService.guardarHorario(horario));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleFormatError(HttpMessageNotReadableException ex) {
        String message = "Error en el formato del JSON. Asegúrate de usar fechas y horas con el formato correcto.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
