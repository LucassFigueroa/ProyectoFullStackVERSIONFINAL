package com.fitlife.inventario.controller;

import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.service.inventarioservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
public class inventariocontroller {

    @Autowired // Inyecta el servicio de inventario
    private inventarioservice inventarioService;

    @PostMapping // Crear nuevo inventario (sin ID en body)
    public ResponseEntity<?> crearInventario(@Valid @RequestBody inventariomodel inventario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        if (inventario.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No debes incluir el ID al crear un inventario."));
        }
        return ResponseEntity.ok(inventarioService.crearInventario(inventario));
    }

    @GetMapping // Listar todos
    public List<inventariomodel> listarInventarios() {
        return inventarioService.listarInventarios();
    }

    @GetMapping("/{id}") // Buscar por ID
    public ResponseEntity<?> obtenerInventario(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            inventariomodel inventario = inventarioService.obtenerPorId(idLong);
            if (inventario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Inventario no encontrado"));
            }
            return ResponseEntity.ok(inventario);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "El ID proporcionado no es válido"));
        }
    }

    @PutMapping("/{id}") // Actualizar existente
    public ResponseEntity<?> actualizarInventario(
            @PathVariable Long id,
            @Valid @RequestBody inventariomodel inventario,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        inventario.setId(id); // Asegura el ID de la URL
        return ResponseEntity.ok(inventarioService.actualizarInventario(id, inventario));
    }

    @DeleteMapping("/{id}") // Eliminar por ID
    public ResponseEntity<?> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarNombre") // Buscar por nombre parcial
    public List<inventariomodel> buscarPorNombre(@RequestParam String nombre) {
        return inventarioService.buscarPorNombre(nombre);
    }

    @GetMapping("/buscarEstado") // Buscar por estado
    public List<inventariomodel> buscarPorEstado(@RequestParam String estado) {
        return inventarioService.buscarPorEstado(estado);
    }

    @GetMapping("/buscarFecha") // Buscar por fecha exacta
    public List<inventariomodel> buscarPorFecha(@RequestParam String fecha) {
        LocalDate date = LocalDate.parse(fecha);
        return inventarioService.buscarPorFecha(date);
    }

    @GetMapping("/buscarRango") // Buscar por rango de fechas
    public List<inventariomodel> buscarPorRangoFecha(
            @RequestParam String desde,
            @RequestParam String hasta) {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        return inventarioService.buscarPorRangoFecha(d1, d2);
    }

    @GetMapping("/buscarAvanzado") // Buscar por nombre + estado
    public List<inventariomodel> buscarPorNombreYEstado(
            @RequestParam String nombre,
            @RequestParam String estado) {
        return inventarioService.buscarPorNombreYEstado(nombre, estado);
    }

    @GetMapping("/buscarSerieExacta") // Buscar por número de serie exacto
    public ResponseEntity<?> buscarPorNumeroSerie(@RequestParam String serie) {
        inventariomodel encontrado = inventarioService.buscarPorNumeroSerie(serie);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Número de serie no encontrado"));
        }
        return ResponseEntity.ok(encontrado);
    }

    @GetMapping("/buscarSerieParcial") // Buscar lista por número de serie parcial
    public List<inventariomodel> buscarPorNumeroSerieParcial(@RequestParam String serie) {
        return inventarioService.buscarPorNumeroSerieParcial(serie);
    }

    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
    public ResponseEntity<?> manejarErroresDeDeserializacion(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Los datos enviados son incorrectos o están mal formateados."
        ));
    }
}
