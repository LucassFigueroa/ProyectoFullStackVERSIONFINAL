package com.fitlife.usuario.controller;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.service.usuarioservice;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class usuariocontroller {

    @Autowired
    private usuarioservice usuarioservice;

    @PostMapping("/register")
    public usuariomodel register(@Valid @RequestBody usuariomodel usuario) {
        return usuarioservice.register(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String contrasena = loginData.get("contrasena");

        usuariomodel user = usuarioservice.login(email, contrasena);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login OK ✅");
            response.put("id", user.getId());
            response.put("nombre", user.getNombre());
            response.put("email", user.getEmail());
            response.put("rol", user.getRol());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Credenciales inválidas ❌");
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/usuarios")
    public CollectionModel<EntityModel<usuariomodel>> getAll() {
        List<EntityModel<usuariomodel>> usuarios = usuarioservice.getAll().stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(usuariocontroller.class).getById(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(usuariocontroller.class).getAll()).withRel("all-usuarios")
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
                linkTo(methodOn(usuariocontroller.class).getAll()).withSelfRel());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<EntityModel<usuariomodel>> getById(@PathVariable Long id) {
        usuariomodel usuario = usuarioservice.getById(id);
        if (usuario != null) {
            EntityModel<usuariomodel> resource = EntityModel.of(usuario,
                    linkTo(methodOn(usuariocontroller.class).getById(id)).withSelfRel(),
                    linkTo(methodOn(usuariocontroller.class).getAll()).withRel("all-usuarios"),
                    linkTo(methodOn(usuariocontroller.class).delete(id)).withRel("delete"),
                    linkTo(methodOn(usuariocontroller.class).update(id, usuario)).withRel("update")
            );
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<usuariomodel> update(@PathVariable Long id, @Valid @RequestBody usuariomodel details) {
        usuariomodel updated = usuarioservice.update(id, details);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = usuarioservice.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
