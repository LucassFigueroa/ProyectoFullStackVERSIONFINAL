package com.fitlife.reserva.controller;

import com.fitlife.reserva.model.reserva;
import com.fitlife.reserva.service.reservaservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/reservas")
public class reservacontroller {

    @Autowired
    private reservaservice reservaservice;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @PostMapping
    public ResponseEntity<EntityModel<reserva>> createReserva(@Valid @RequestBody reserva reserva, Authentication auth) {
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))){
            reserva.setClienteNombre(auth.getName());
        }
        reserva saved = reservaservice.saveReserva(reserva);
        EntityModel<reserva> resource = toModel(saved);
        return ResponseEntity.ok(resource);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping
    public CollectionModel<EntityModel<reserva>> getAllReservas(Authentication auth) {
        List<reserva> lista;

        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))){
            lista = reservaservice.getReservasByClienteNombre(auth.getName());
        } else {
            lista = reservaservice.getAllReservas();
        }

        List<EntityModel<reserva>> reservas = lista.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(reservacontroller.class).getAllReservas(auth)).withSelfRel());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<reserva>> getReservaById(@PathVariable Long id, Authentication auth) {
        return reservaservice.getReservaById(id)
                .filter(res -> {
                    if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))){
                        return res.getClienteNombre().equals(auth.getName());
                    }
                    return true;
                })
                .map(res -> ResponseEntity.ok(toModel(res)))
                .orElse(ResponseEntity.status(403).build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<reserva> updateReserva(@PathVariable Long id, @Valid @RequestBody reserva reserva) {
        reserva updated = reservaservice.updateReserva(id, reserva);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        boolean deleted = reservaservice.deleteReserva(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Método privado para crear EntityModel sin pasar Authentication (evita error HATEOAS)
    private EntityModel<reserva> toModel(reserva res) {
        return EntityModel.of(res,
                linkTo(methodOn(reservacontroller.class).getReservaById(res.getId(), null)).withSelfRel(),
                linkTo(methodOn(reservacontroller.class).getAllReservas(null)).withRel("all-reservas"));
    }
}
