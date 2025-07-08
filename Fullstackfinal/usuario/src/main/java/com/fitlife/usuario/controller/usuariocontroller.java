package com.fitlife.usuario.controller;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.service.usuarioservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Controlador de Usuario", description = "Operaciones CRUD del microservicio Usuario")
public class usuariocontroller {

    @Autowired
    private usuarioservice usuarioservice;

    @Operation(summary = "Obtener todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Usuarios encontrados")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CollectionModel<EntityModel<usuariomodel>>> getAllUsuarios() {
        List<usuariomodel> usuarios = usuarioservice.getAll();

        List<EntityModel<usuariomodel>> usuariosConLinks = usuarios.stream()
            .map(usuario -> EntityModel.of(usuario,
                linkTo(methodOn(usuariocontroller.class).getUsuarioById(usuario.getId())).withSelfRel(),
                linkTo(methodOn(usuariocontroller.class).getAllUsuarios()).withRel("usuarios")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(usuariosConLinks));
    }

    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<EntityModel<usuariomodel>> getUsuarioById(@PathVariable long id) {
        usuariomodel usuario = usuarioservice.getById(id);

        return ResponseEntity.ok(EntityModel.of(usuario,
            linkTo(methodOn(usuariocontroller.class).getUsuarioById(id)).withSelfRel(),
            linkTo(methodOn(usuariocontroller.class).getAllUsuarios()).withRel("usuarios")));
    }

    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<usuariomodel>> createUsuario(@Valid @RequestBody usuariomodel usuario) {
        usuariomodel nuevoUsuario = usuarioservice.register(usuario);

        return ResponseEntity.status(201).body(EntityModel.of(nuevoUsuario,
            linkTo(methodOn(usuariocontroller.class).getUsuarioById(nuevoUsuario.getId())).withSelfRel(),
            linkTo(methodOn(usuariocontroller.class).getAllUsuarios()).withRel("usuarios")));
    }

    @Operation(summary = "Actualizar un usuario por ID")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<EntityModel<usuariomodel>> updateUsuario(@PathVariable long id, @Valid @RequestBody usuariomodel usuario) {
        usuariomodel actualizado = usuarioservice.update(id, usuario);

        return ResponseEntity.ok(EntityModel.of(actualizado,
            linkTo(methodOn(usuariocontroller.class).getUsuarioById(id)).withSelfRel(),
            linkTo(methodOn(usuariocontroller.class).getAllUsuarios()).withRel("usuarios")));
    }

    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable long id) {
        usuarioservice.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Login de usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("permitAll()")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String contrasena = credenciales.get("contrasena");

        usuariomodel usuario = usuarioservice.login(email, contrasena);

        if (usuario != null) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("message", "Login OK ✅");
            respuesta.put("rol", usuario.getRol());
            return ResponseEntity.ok(respuesta);
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }
    }
}
