package com.fitlife.clase.controller;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.service.claseservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clases")
public class clasecontroller {

    @Autowired
    private claseservice claseservice;

    // ✅ Crear clase: solo ADMIN o STAFF
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public EntityModel<clase> guardarClase(@RequestBody clase clase) {
        clase creada = claseservice.guardarClase(clase);
        return EntityModel.of(creada,
                linkTo(methodOn(clasecontroller.class).guardarClase(clase)).withSelfRel(),
                linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases"));
    }

    // ✅ Ver todas: todos los roles
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping
    public CollectionModel<EntityModel<clase>> obtenerTodasLasClases() {
        List<EntityModel<clase>> clases = claseservice.obtenerTodasLasClases().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(clasecontroller.class).obtenerClasePorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases")
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(clases, linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withSelfRel());
    }

    // ✅ Ver una: todos los roles
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping("/{id}")
    public EntityModel<clase> obtenerClasePorId(@PathVariable Long id) {
        clase encontrada = claseservice.obtenerClasePorId(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        return EntityModel.of(encontrada,
                linkTo(methodOn(clasecontroller.class).obtenerClasePorId(id)).withSelfRel(),
                linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases"));
    }

    // ✅ Actualizar: ADMIN o STAFF
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{id}")
    public EntityModel<clase> actualizarClase(@PathVariable Long id, @RequestBody clase claseDetails) {
        clase actualizada = claseservice.actualizarClase(id, claseDetails);
        return EntityModel.of(actualizada,
                linkTo(methodOn(clasecontroller.class).obtenerClasePorId(id)).withSelfRel());
    }

    // ✅ Eliminar: solo ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminarClase(@PathVariable Long id) {
        claseservice.eliminarClase(id);
    }
}
