package com.fitlife.reporte.controller;

import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.service.reporteservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Reportes", description = "Operaciones relacionadas con reportes")
@RestController
@RequestMapping("/api")
public class reportecontroller {

    @Autowired
    private reporteservice reporteservice;

    @Operation(summary = "Crear un nuevo reporte")
    @PostMapping("/reportes")
    public reportemodel create(@Valid @RequestBody reportemodel reporte) {
        return reporteservice.save(reporte);
    }

    @Operation(summary = "Obtener todos los reportes")
    @GetMapping("/reportes")
    public List<reportemodel> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null) {
            Page<reportemodel> pagina = reporteservice.getAll(PageRequest.of(page, size));
            return pagina.getContent();
        } else {
            return reporteservice.getAll();
        }
    }

    @Operation(summary = "Obtener reporte por ID")
    @GetMapping("/reportes/{id}")
    public ResponseEntity<reportemodel> getById(@PathVariable Long id) {
        reportemodel reporte = reporteservice.getById(id);
        return (reporte != null)
                ? ResponseEntity.ok(reporte)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar reporte por ID")
    @PutMapping("/reportes/{id}")
    public ResponseEntity<reportemodel> update(
            @PathVariable Long id,
            @Valid @RequestBody reportemodel details) {

        reportemodel updated = reporteservice.update(id, details);
        return (updated != null)
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar reporte por ID")
    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = reporteservice.delete(id);
        return (deleted)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Filtrar reportes por tipo")
    @GetMapping("/reportes/filtro/tipo")
    public List<reportemodel> getByTipo(@RequestParam String tipo) {
        return reporteservice.getByTipo(tipo);
    }

    @Operation(summary = "Filtrar reportes por rango de fechas")
    @GetMapping("/reportes/filtro/fecha")
    public List<reportemodel> getByFecha(
            @RequestParam String desde,
            @RequestParam String hasta) {


        LocalDateTime fechaDesde = LocalDateTime.parse(desde);
        LocalDateTime fechaHasta = LocalDateTime.parse(hasta);
        return reporteservice.getByFechaCreacionBetween(fechaDesde, fechaHasta);
    }
}
