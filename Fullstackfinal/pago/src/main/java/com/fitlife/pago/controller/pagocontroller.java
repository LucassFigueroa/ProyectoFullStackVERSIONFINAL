package com.fitlife.pago.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.service.pagoservice;

@RestController
@RequestMapping("/pagos")
public class pagocontroller {

    private final pagoservice pagoService;

    public pagocontroller(pagoservice pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<?> registrarPago(@Valid @RequestBody pagomodel pago, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }

        if (pago.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No debes incluir el ID al crear un pago."));
        }

        return ResponseEntity.ok(pagoService.guardarPago(pago));
    }

    @GetMapping
    public List<pagomodel> listarPagos() {
        return pagoService.obtenerPagos();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<pagomodel> pagoPorCliente(@PathVariable Long idCliente) {
        return pagoService.obtenerPagosPorCliente(idCliente);
    }

}
