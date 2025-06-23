package com.fitlife.soporte.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class soportemodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String estado; 

    @Column(nullable = false)
    private String cliente; 
}
