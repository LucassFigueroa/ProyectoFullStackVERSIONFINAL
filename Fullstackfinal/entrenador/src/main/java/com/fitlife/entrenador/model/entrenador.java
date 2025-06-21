package com.fitlife.entrenador.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "entrenadores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String nombre;

    @NotBlank(message = "La especialidad no puede estar vacía")
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String especialidad;
}
