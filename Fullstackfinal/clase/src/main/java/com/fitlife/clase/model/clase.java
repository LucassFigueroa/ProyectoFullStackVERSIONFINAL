package com.fitlife.clase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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

    @NotBlank(message = "El nombre de la clase no puede estar vacío")
    @Size(max = 50, message = "El nombre de la clase no puede superar 50 caracteres")
    @Column(length = 50, nullable = false, unique = true)
    private String nombreClase;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 200, message = "La descripción no puede superar 200 caracteres")
    @Column(length = 200, nullable = false)
    private String descripcion;

    @Min(value = 15, message = "La duración mínima es de 15 minutos")
    @Max(value = 180, message = "La duración máxima es de 180 minutos")
    @Column(nullable = false)
    private int duracion; // en minutos

    @Min(value = 1, message = "Debe haber al menos 1 cupo disponible")
    @Max(value = 100, message = "Capacidad máxima es de 100 personas")
    @Column(nullable = false)
    private int capacidadMaxima;

    @NotBlank(message = "El nivel no puede estar vacío")
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String nivel; // Básico, Intermedio, Avanzado

    @NotNull(message = "El ID del entrenador es obligatorio")
    @Column(nullable = false)
    private Long entrenadorId; // FK simple al microservicio entrenador
}
