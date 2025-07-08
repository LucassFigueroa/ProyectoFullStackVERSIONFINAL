package com.fitlife.pago.controller;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.service.pagoservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/pago")
@Tag(name = "Controlador de Pagos", description = "Operaciones relacionadas con los pagos")
public class pagocontroller {

    @Autowired
    private pagoservice pagoservice;

    @Operation(summary = "Crear un nuevo pago")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pago creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public ResponseEntity<EntityModel<pagomodel>> crearPago(@Valid @RequestBody pagomodel pago) {
        pagomodel creado = pagoservice.crearPago(pago);
        return ResponseEntity.status(201).body(toModel(creado));
    }

    @Operation(summary = "Obtener un pago por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<pagomodel>> obtenerPago(@PathVariable Long id) {
        pagomodel pago = pagoservice.obtenerPorId(id);
        if (pago == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(toModel(pago));
    }

    @Operation(summary = "Listar todos los pagos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida"),
        @ApiResponse(responseCode = "206", description = "Lista vacía"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<pagomodel>>> listarPagos() {
        List<pagomodel> lista = pagoservice.listarPagos();

        if (lista.isEmpty()) {
            return ResponseEntity.status(206).body(
                CollectionModel.empty(linkTo(methodOn(pagocontroller.class).listarPagos()).withSelfRel())
            );
        }

        List<EntityModel<pagomodel>> pagosConLinks = lista.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(pagosConLinks,
                linkTo(methodOn(pagocontroller.class).listarPagos()).withSelfRel()
            )
        );
    }

    @Operation(summary = "Actualizar un pago por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<pagomodel>> actualizarPago(@PathVariable Long id, @Valid @RequestBody pagomodel pago) {
        if (pagoservice.obtenerPorId(id) == null) {
            return ResponseEntity.status(404).build();
        }

        pagomodel actualizado = pagoservice.actualizarPago(id, pago);
        return ResponseEntity.ok(toModel(actualizado));
    }

    @Operation(summary = "Eliminar un pago por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        if (pagoservice.obtenerPorId(id) == null) {
            return ResponseEntity.status(404).build();
        }

        pagoservice.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar pagos por estado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
        @ApiResponse(responseCode = "204", description = "No se encontraron pagos"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/buscarEstado")
    public ResponseEntity<List<pagomodel>> buscarPorEstado(@RequestParam String estado) {
        List<pagomodel> resultados = pagoservice.buscarPorEstado(estado);
        if (resultados.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultados);
    }

    @Operation(summary = "Buscar pagos por rango de fecha")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
        @ApiResponse(responseCode = "204", description = "No se encontraron pagos"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/buscarRangoFecha")
    public ResponseEntity<List<pagomodel>> buscarPorRangoFecha(@RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        List<pagomodel> resultados = pagoservice.buscarPorRangoFecha(desde, hasta);
        if (resultados.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultados);
    }

    @Operation(summary = "Buscar pagos por método de pago")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
        @ApiResponse(responseCode = "204", description = "No se encontraron pagos"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/buscarMetodo")
    public ResponseEntity<List<pagomodel>> buscarPorMetodo(@RequestParam String metodo) {
        List<pagomodel> resultados = pagoservice.buscarPorMetodo(metodo);
        if (resultados.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultados);
    }

    @Operation(summary = "Buscar pagos con monto mayor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
        @ApiResponse(responseCode = "204", description = "No se encontraron pagos"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/buscarMontoMayor")
    public ResponseEntity<List<pagomodel>> buscarPorMontoMayor(@RequestParam Integer monto) {
        List<pagomodel> resultados = pagoservice.buscarPorMontoMayor(monto);
        if (resultados.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultados);
    }

    @Operation(summary = "Buscar pagos con monto menor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
        @ApiResponse(responseCode = "204", description = "No se encontraron pagos"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/buscarMontoMenor")
    public ResponseEntity<List<pagomodel>> buscarPorMontoMenor(@RequestParam Integer monto) {
        List<pagomodel> resultados = pagoservice.buscarPorMontoMenor(monto);
        if (resultados.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultados);
    }

    // Método auxiliar HATEOAS
    private EntityModel<pagomodel> toModel(pagomodel pago) {
        return EntityModel.of(pago,
            linkTo(methodOn(pagocontroller.class).obtenerPago(pago.getId())).withSelfRel(),
            linkTo(methodOn(pagocontroller.class).listarPagos()).withRel("todos-los-pagos")
        );
    }
}
