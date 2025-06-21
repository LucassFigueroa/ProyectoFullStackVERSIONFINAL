package com.fitlife.horario.controller;

import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.service.horarioservice;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/horarios")
public class horariocontroller {

    @Autowired
    private horarioservice horarioService;

    // Lista todos los Horarios
    @GetMapping
    public List<horariomodel> listarHorarios() {
        return horarioService.listarHorarios();
    }

    // Busca Horario por Id
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerHorarioPorId(@PathVariable Long id) {
        Optional<horariomodel> horario = horarioService.obtenerPorId(id);
        if (horario.isPresent()) {
            return ResponseEntity.ok(horario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Horario no encontrado"));
        }
    }

    // Crea un nuevo horario Validando
    @PostMapping
    public ResponseEntity<?> guardarHorario(@Valid @RequestBody horariomodel horario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(horarioService.guardarHorario(horario));
    }

    // Actualiza horario existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarHorario(@PathVariable Long id, @Valid @RequestBody horariomodel horario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(horarioService.actualizarHorario(id, horario));
    }

    // Elimina Horario por Id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }

    // Filtra horarios por ID de entrenador.
    @GetMapping("/buscarPorEntrenador")
    public List<horariomodel> buscarPorEntrenador(@RequestParam Long entrenadorId) {
        return horarioService.buscarPorEntrenador(entrenadorId);
    }

    // Filtra horarios en un rango de fecha y hora de inicio.
    @GetMapping("/buscarPorRango")
    public ResponseEntity<?> buscarPorRangoFechaHora(
            @RequestParam String desde,
            @RequestParam String hasta) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(desde);
            LocalDateTime fin = LocalDateTime.parse(hasta);
            return ResponseEntity.ok(horarioService.buscarPorRangoFechaHora(inicio, fin));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Formato de fecha/hora incorrecto. Usa yyyy-MM-ddTHH:mm"
            ));
        }
    }

    // Filtra por entrenador + rango de fecha/hora
    @GetMapping("/buscarPorEntrenadorYRango")
    public ResponseEntity<?> buscarPorEntrenadorYRango(
            @RequestParam Long entrenadorId,
            @RequestParam String desde,
            @RequestParam String hasta) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(desde);
            LocalDateTime fin = LocalDateTime.parse(hasta);
            return ResponseEntity.ok(horarioService.buscarPorEntrenadorYRango(entrenadorId, inicio, fin));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Formato de fecha/hora incorrecto. Usa yyyy-MM-ddTHH:mm"
            ));
        }
    }

    // Error si JSON está mal escrito
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleFormatError(HttpMessageNotReadableException ex) {
        String message = "Error en el formato del JSON. Asegúrate de usar fechas y horas con el formato ISO correcto (yyyy-MM-ddTHH:mm).";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}