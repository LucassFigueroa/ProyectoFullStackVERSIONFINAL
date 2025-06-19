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

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String especialidad;
}
