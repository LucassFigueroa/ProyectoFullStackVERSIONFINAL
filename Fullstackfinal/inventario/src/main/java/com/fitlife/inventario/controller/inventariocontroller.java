package com.fitlife.inventario.controller;

import com.fitlife.inventario.model.inventariomodel;
import com.fitlife.inventario.service.inventarioservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventario")
@Tag(name = "Controlador de Inventario", description = "Operaciones CRUD y búsquedas avanzadas para artículos del inventario de FitLife SPA")
public class inventariocontroller {

    @Autowired
    private inventarioservice inventarioService;

    @Operation(summary = "Crear un nuevo artículo de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artículo creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> crearInventario(@Valid @RequestBody inventariomodel inventario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        if (inventario.getId() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No debes incluir el ID al crear un inventario."));
        }
        inventariomodel creado = inventarioService.crearInventario(inventario);
        EntityModel<inventariomodel> recurso = EntityModel.of(creado,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).obtenerInventario(creado.getId().toString())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).listarInventarios()).withRel("inventarios")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Listar todos los artículos de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<inventariomodel>>> listarInventarios() {
        List<inventariomodel> lista = inventarioService.listarInventarios();

        List<EntityModel<inventariomodel>> inventarios = lista.stream()
                .map(inv -> EntityModel.of(inv,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).obtenerInventario(inv.getId().toString())).withSelfRel()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(inventarios,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).listarInventarios()).withSelfRel()
        ));
    }

    @Operation(summary = "Obtener un artículo de inventario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artículo encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInventario(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            inventariomodel inventario = inventarioService.obtenerPorId(idLong);
            if (inventario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Inventario no encontrado"));
            }
            EntityModel<inventariomodel> recurso = EntityModel.of(inventario,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).obtenerInventario(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).listarInventarios()).withRel("inventarios")
            );
            return ResponseEntity.ok(recurso);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "El ID proporcionado no es válido"));
        }
    }

    @Operation(summary = "Actualizar un artículo de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artículo actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInventario(
            @PathVariable Long id,
            @Valid @RequestBody inventariomodel inventario,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        inventario.setId(id);
        inventariomodel actualizado = inventarioService.actualizarInventario(id, inventario);

        EntityModel<inventariomodel> recurso = EntityModel.of(actualizado,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).obtenerInventario(id.toString())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(inventariocontroller.class).listarInventarios()).withRel("inventarios")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Eliminar un artículo de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Artículo eliminado correctamente")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar por nombre de artículo")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/buscarNombre")
    public List<inventariomodel> buscarPorNombre(@RequestParam String nombre) {
        return inventarioService.buscarPorNombre(nombre);
    }

    @Operation(summary = "Buscar por estado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/buscarEstado")
    public List<inventariomodel> buscarPorEstado(@RequestParam String estado) {
        return inventarioService.buscarPorEstado(estado);
    }

    @Operation(summary = "Buscar por fecha exacta de ingreso")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/buscarFecha")
    public List<inventariomodel> buscarPorFecha(@RequestParam String fecha) {
        LocalDate date = LocalDate.parse(fecha);
        return inventarioService.buscarPorFecha(date);
    }

    @Operation(summary = "Buscar por rango de fechas")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/buscarRango")
    public List<inventariomodel> buscarPorRangoFecha(@RequestParam String desde, @RequestParam String hasta) {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        return inventarioService.buscarPorRangoFecha(d1, d2);
    }

    @Operation(summary = "Buscar por nombre y estado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/buscarAvanzado")
    public List<inventariomodel> buscarPorNombreYEstado(@RequestParam String nombre, @RequestParam String estado) {
        return inventarioService.buscarPorNombreYEstado(nombre, estado);
    }

    @Operation(summary = "Buscar por número de serie exacto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artículo encontrado"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/buscarSerieExacta")
    public ResponseEntity<?> buscarPorNumeroSerie(@RequestParam String serie) {
        inventariomodel encontrado = inventarioService.buscarPorNumeroSerie(serie);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Número de serie no encontrado"));
        }
        return ResponseEntity.ok(encontrado);
    }

    @Operation(summary = "Buscar por coincidencia parcial del número de serie")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/buscarSerieParcial")
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
