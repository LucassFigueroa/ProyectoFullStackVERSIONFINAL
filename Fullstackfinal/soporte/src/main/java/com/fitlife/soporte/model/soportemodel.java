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

    @NotBlank(message = "El asunto no puede estar vacío")
    @Size(max = 50, message = "El asunto no debe superar los 50 caracteres")
    @Column(length = 50, nullable = false)
    private String asunto;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 255, message = "El mensaje no debe superar los 255 caracteres")
    @Column(length = 255, nullable = false)
    private String mensaje;
}
