package com.fitlife.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventario")
public class inventariomodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del artículo", example = "1")
    private Long id;

    @NotBlank(message = "El nombre del artículo es obligatorio")
    @Size(max = 50)
    @Column(name = "nombre_articulo", nullable = false, length = 50)
    @Schema(description = "Nombre del artículo", example = "Bicicleta estática")
    private String nombreArticulo;

    @NotBlank(message = "El número de serie es obligatorio")
    @Size(max = 30)
    @Column(name = "numero_serie", nullable = false, unique = true, length = 30)
    @Schema(description = "Número de serie del artículo", example = "SER12345XYZ")
    private String numeroSerie;

    @Min(value = 0)
    @Column(nullable = false)
    @Schema(description = "Cantidad en inventario", example = "5")
    private Integer cantidad;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    @Schema(description = "Estado del artículo", example = "Disponible")
    private String estado;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(name = "fecha_ingreso", nullable = false)
    @Schema(description = "Fecha de ingreso del artículo", example = "2025-07-08")
    private LocalDate fechaIngreso;
}
