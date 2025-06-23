package com.fitlife.evaluacionfisica.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "evaluaciones_fisicas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class evaluacionfisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clienteNombre;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private Double altura;

    @Column(nullable = false)
    private Double imc;

    @Column(nullable = false)
    private String evaluador;

    @Column(nullable = false)
    private LocalDate fechaevaluacion; // ✅ NUEVO CAMPO
}
