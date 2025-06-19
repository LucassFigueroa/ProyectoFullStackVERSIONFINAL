package com.fitlife.reserva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class reservamodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long usuarioid;

    @NotNull
    @Column(nullable = false)
    private Long claseid;

    @NotNull
    @Column(nullable = false)
    private String fecha;

    @NotNull
    @Column(nullable = false)
    private String hora;
}
