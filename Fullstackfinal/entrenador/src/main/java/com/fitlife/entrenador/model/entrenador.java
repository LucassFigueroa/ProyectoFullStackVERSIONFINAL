package com.fitlife.entrenador.model;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "entrenadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especialidad; //

    @Column(nullable = false)
    private String experiencia; // años de experiencia

}
