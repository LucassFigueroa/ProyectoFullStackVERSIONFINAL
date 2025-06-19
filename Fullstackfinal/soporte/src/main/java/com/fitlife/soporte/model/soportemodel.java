package com.fitlife.soporte.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "soportes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class soportemodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String asunto;

    @NotBlank
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String mensaje;
}
