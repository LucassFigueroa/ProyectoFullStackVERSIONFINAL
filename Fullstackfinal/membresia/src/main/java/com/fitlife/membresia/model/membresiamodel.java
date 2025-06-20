package com.fitlife.membresia.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class membresiamodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El tipo de membresía es obligatorio")
    private String tipo;

    @NotBlank(message = "La fecha de inicio es obligatoria")
    private String fechaInicio;

    @NotBlank(message = "La fecha de fin es obligatoria")
    private String fechaFin;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    public List<membresiamodel> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    public membresiamodel save(membresiamodel membresia) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}
