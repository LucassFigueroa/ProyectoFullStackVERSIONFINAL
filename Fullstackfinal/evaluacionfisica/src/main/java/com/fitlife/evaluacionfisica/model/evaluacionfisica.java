package com.fitlife.evaluacionfisica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "evaluaciones_fisicas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class evaluacionfisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long usuarioid;

    @Positive
    @Column(nullable = false)
    private Double peso;

    @Positive
    @Column(nullable = false)
    private Double altura;

    @Positive
    @Column(nullable = false)
    private Double imc;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaevaluacion;

    @Size(max = 255)
    private String observaciones;
}
