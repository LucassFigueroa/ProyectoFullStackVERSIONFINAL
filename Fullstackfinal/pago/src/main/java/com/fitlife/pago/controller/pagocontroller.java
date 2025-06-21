package com.fitlife.pago.controller;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.service.pagoservice;
import jakarta.validation.Valid;
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
@RequestMapping("/pagos") 
public class pagocontroller {

    private final pagoservice pagoService;

    // Constructor para inyección
    public pagocontroller(pagoservice pagoService) {
        this.pagoService = pagoService;
    }

    // Registrar pago
    @PostMapping
    public ResponseEntity<?> registrarPago(@Valid @RequestBody pagomodel pago, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        if (pago.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No debes incluir el ID al crear un pago."));
        }

        return ResponseEntity.ok(pagoService.guardarPago(pago));
    }

    // Listar todos los pagos
    @GetMapping
    public List<pagomodel> listarPagos() {
        return pagoService.obtenerPagos();
    }

    // Obtener pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPagoPorId(@PathVariable Long id) {
        Optional<pagomodel> pago = pagoService.obtenerPorId(id);

        if (pago.isPresent()) {
            return ResponseEntity.ok(pago.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Pago no encontrado"));
        }
    }



    // Actualizar pago
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPago(@PathVariable Long id,
                                            @Valid @RequestBody pagomodel pago,
                                            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(pagoService.actualizarPago(id, pago));
    }

    // Eliminar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    // Filtrar pagos por cliente
    @GetMapping("/cliente/{idCliente}")
    public List<pagomodel> pagoPorCliente(@PathVariable Long idCliente) {
        return pagoService.obtenerPagosPorCliente(idCliente);
    }

    // Filtrar pagos por estado
    @GetMapping("/estado")
    public List<pagomodel> pagoPorEstado(@RequestParam String estado) {
        return pagoService.obtenerPagosPorEstado(estado);
    }

    // Filtrar pagos por cliente y estado
    @GetMapping("/clienteEstado")
    public List<pagomodel> pagoPorClienteYEstado(@RequestParam Long idCliente,
                                                 @RequestParam String estado) {
        return pagoService.obtenerPagosPorClienteYEstado(idCliente, estado);
    }

    // Manejador de JSON mal formado
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> manejarErroresJson(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Los datos enviados no tienen el formato correcto o están mal formateados."
        ));
    }
}
