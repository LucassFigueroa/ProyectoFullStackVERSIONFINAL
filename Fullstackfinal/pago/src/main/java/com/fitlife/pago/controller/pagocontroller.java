package com.fitlife.pago.controller;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.service.pagoservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pago")
public class pagocontroller {

    @Autowired
    private pagoservice pagoService;

    @PostMapping
    public ResponseEntity<?> crearPago(@Valid @RequestBody pagomodel pago, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .collect(Collectors.toMap(
                                    err -> err.getField(),
                                    err -> err.getDefaultMessage()
                            ))
            );
        }
        return ResponseEntity.ok(toModel(pagoService.crearPago(pago)));
    }

    @GetMapping
    public CollectionModel<EntityModel<pagomodel>> listarPagos() {
        List<EntityModel<pagomodel>> pagos = pagoService.listarPagos().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(pagos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(pagocontroller.class).listarPagos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPago(@PathVariable Long id) {
        pagomodel pago = pagoService.obtenerPorId(id);
        if (pago == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Pago no encontrado"));
        }
        return ResponseEntity.ok(toModel(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPago(@PathVariable Long id,
                                            @Valid @RequestBody pagomodel pago,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .collect(Collectors.toMap(
                                    err -> err.getField(),
                                    err -> err.getDefaultMessage()
                            ))
            );
        }
        return ResponseEntity.ok(toModel(pagoService.actualizarPago(id, pago)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarEstado")
    public List<pagomodel> buscarPorEstado(@RequestParam String estado) {
        return pagoService.buscarPorEstado(estado);
    }

    @GetMapping("/buscarRangoFecha")
    public List<pagomodel> buscarPorRangoFecha(@RequestParam String desde,
                                               @RequestParam String hasta) {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        return pagoService.buscarPorRangoFecha(d1, d2);
    }

    @GetMapping("/buscarMetodo")
    public List<pagomodel> buscarPorMetodo(@RequestParam String metodo) {
        return pagoService.buscarPorMetodo(metodo);
    }

    @GetMapping("/buscarMontoMayor")
    public List<pagomodel> buscarPorMontoMayor(@RequestParam Double monto) {
        return pagoService.buscarPorMontoMayor(monto);
    }

    @GetMapping("/buscarMontoMenor")
    public List<pagomodel> buscarPorMontoMenor(@RequestParam Double monto) {
        return pagoService.buscarPorMontoMenor(monto);
    }

    private EntityModel<pagomodel> toModel(pagomodel pago) {
        return EntityModel.of(pago,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(pagocontroller.class).obtenerPago(pago.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(pagocontroller.class).listarPagos()).withRel("all-pagos"));
    }
}
