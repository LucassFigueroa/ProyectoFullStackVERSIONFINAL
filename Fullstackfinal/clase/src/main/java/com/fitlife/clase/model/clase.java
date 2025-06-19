package com.fitlife.clase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clases")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String nombreClase;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String descripcion;
}
