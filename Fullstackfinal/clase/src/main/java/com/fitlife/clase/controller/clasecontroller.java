package com.fitlife.clase.controller;

import com.fitlife.clase.model.clase;
import com.fitlife.clase.service.claseservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clases")
public class clasecontroller {

    @Autowired
    private claseservice claseservice;

    // Crear clase
    @PostMapping
    public EntityModel<clase> guardarClase(@RequestBody clase clase) {
        clase creada = claseservice.guardarClase(clase);
        return EntityModel.of(creada,
                linkTo(methodOn(clasecontroller.class).guardarClase(clase)).withSelfRel(),
                linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases"));
    }

    // Obtener todas
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

    // Obtener una por ID
    @GetMapping("/{id}")
    public EntityModel<clase> obtenerClasePorId(@PathVariable Long id) {
        clase encontrada = claseservice.obtenerClasePorId(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        return EntityModel.of(encontrada,
                linkTo(methodOn(clasecontroller.class).obtenerClasePorId(id)).withSelfRel(),
                linkTo(methodOn(clasecontroller.class).obtenerTodasLasClases()).withRel("todas-las-clases"));
    }

    // Actualizar
    @PutMapping("/{id}")
    public EntityModel<clase> actualizarClase(@PathVariable Long id, @RequestBody clase claseDetails) {
        clase actualizada = claseservice.actualizarClase(id, claseDetails);
        return EntityModel.of(actualizada,
                linkTo(methodOn(clasecontroller.class).obtenerClasePorId(id)).withSelfRel());
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public void eliminarClase(@PathVariable Long id) {
        claseservice.eliminarClase(id);
    }
}
