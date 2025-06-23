package com.fitlife.reporte.controller;

import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.service.reporteservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Reportes", description = "Operaciones relacionadas con reportes")
@RestController
@RequestMapping("/api/reportes")
public class reportecontroller {

    @Autowired
    private reporteservice reporteservice;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Crear un nuevo reporte")
    public ResponseEntity<EntityModel<reportemodel>> create(@Valid @RequestBody reportemodel reporte) {
        reportemodel saved = reporteservice.save(reporte);
        return ResponseEntity.ok(toModel(saved));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOPORTE')")
    @GetMapping
    @Operation(summary = "Obtener todos los reportes")
    public CollectionModel<EntityModel<reportemodel>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        List<reportemodel> reportes;

        if (page != null && size != null) {
            Page<reportemodel> pagina = reporteservice.getAll(PageRequest.of(page, size));
            reportes = pagina.getContent();
        } else {
            reportes = reporteservice.getAll();
        }

        List<EntityModel<reportemodel>> modelos = reportes.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelos,
                linkTo(methodOn(reportecontroller.class).getAll(page, size)).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOPORTE')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener reporte por ID")
    public ResponseEntity<EntityModel<reportemodel>> getById(@PathVariable Long id) {
        reportemodel reporte = reporteservice.getById(id);
        return reporte != null
                ? ResponseEntity.ok(toModel(reporte))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOPORTE')")
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reporte por ID")
    public ResponseEntity<EntityModel<reportemodel>> update(
            @PathVariable Long id,
            @Valid @RequestBody reportemodel details) {

        reportemodel updated = reporteservice.update(id, details);
        return updated != null
                ? ResponseEntity.ok(toModel(updated))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reporte por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = reporteservice.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOPORTE')")
    @GetMapping("/filtro/tipo")
    @Operation(summary = "Filtrar reportes por tipo")
    public CollectionModel<EntityModel<reportemodel>> getByTipo(@RequestParam String tipo) {
        List<reportemodel> reportes = reporteservice.getByTipo(tipo);

        List<EntityModel<reportemodel>> modelos = reportes.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelos,
                linkTo(methodOn(reportecontroller.class).getByTipo(tipo)).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOPORTE')")
    @GetMapping("/filtro/fecha")
    @Operation(summary = "Filtrar reportes por rango de fechas")
    public CollectionModel<EntityModel<reportemodel>> getByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String hasta) {

        LocalDateTime fechaDesde = LocalDateTime.parse(desde);
        LocalDateTime fechaHasta = LocalDateTime.parse(hasta);

        List<reportemodel> reportes = reporteservice.getByFechaCreacionBetween(fechaDesde, fechaHasta);

        List<EntityModel<reportemodel>> modelos = reportes.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelos,
                linkTo(methodOn(reportecontroller.class).getByFecha(desde, hasta)).withSelfRel());
    }

    private EntityModel<reportemodel> toModel(reportemodel reporte) {
        return EntityModel.of(reporte,
                linkTo(methodOn(reportecontroller.class).getById(reporte.getId())).withSelfRel(),
                linkTo(methodOn(reportecontroller.class).getAll(null, null)).withRel("todos"),
                linkTo(methodOn(reportecontroller.class).getByTipo(reporte.getTipo())).withRel("tipo"));
    }
}
