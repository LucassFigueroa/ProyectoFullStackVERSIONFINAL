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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
public class inventariocontroller {

    @Autowired // Inyecta automaticamente el servicio y hace que Spring se encarge de crear las instancias 
    private inventarioservice inventarioService;

    @PostMapping //Este método se ejecuta cuando el cliente hace un POST a /inventario
    public ResponseEntity<?> crearInventario(@Valid @RequestBody inventariomodel inventario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();// Crea un mapa con los errores y muestra mensajes al usuario 
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

    // En lista todos los registros 
    @GetMapping
    public List<inventariomodel> listarInventarios() {
        return inventarioService.listarInventarios();
    }

    // get busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInventario(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            inventariomodel inventario = inventarioService.obtenerPorId(idLong);
            if (inventario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Inventario no encontrado"));
            }
            return ResponseEntity.ok(inventario);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El ID proporcionado no es válido"));
        }
    }

    //PUT /inventario/{id}, actualiza la instancia existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInventario(
            @PathVariable Long id,
            @Valid @RequestBody inventariomodel inventario,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        return ResponseEntity.ok(inventarioService.actualizarInventario(id, inventario));
    }

    //elimina registro por Id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }

    //busca por nombre parcialmente
    @GetMapping("/buscarNombre")
    public List<inventariomodel> buscarPorNombre(@RequestParam String nombre) {
        return inventarioService.buscarPorNombre(nombre);
    }

    //busca por estado
    @GetMapping("/buscarEstado")
    public List<inventariomodel> buscarPorEstado(@RequestParam String estado) {
        return inventarioService.buscarPorEstado(estado);
    }

    //busca por fecha exacta
    @GetMapping("/buscarFecha")
    public List<inventariomodel> buscarPorFecha(@RequestParam String fecha) {
        LocalDate date = LocalDate.parse(fecha);
        return inventarioService.buscarPorFecha(date);
    }

    //busca por rango de fechas 
    @GetMapping("/buscarRango")
    public List<inventariomodel> buscarPorRangoFecha(
            @RequestParam String desde,
            @RequestParam String hasta) {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        return inventarioService.buscarPorRangoFecha(d1, d2);
    }

    //get buscar  nombre y estado 
    @GetMapping("/buscarAvanzado")
    public List<inventariomodel> buscarPorNombreYEstado(
            @RequestParam String nombre,
            @RequestParam String estado) {
        return inventarioService.buscarPorNombreYEstado(nombre, estado);
    }

    //Error si JSON está mal escrito
    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
    public ResponseEntity<?> manejarErroresDeDeserializacion(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Los datos enviados son incorrectos o están mal formateados."
        ));
    }
}