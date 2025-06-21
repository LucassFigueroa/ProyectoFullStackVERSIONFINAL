package com.fitlife.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventario") 
public class inventariomodel {

    @Id // Clave primaria autoincremental
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del artículo es obligatorio")
    @Size(max = 20)
    @Column(name = "nombre_articulo", nullable = false, length = 20)
    private String nombreArticulo;

    @Min(value = 0)
    @Column(nullable = false)
    private Integer cantidad;

    @NotBlank(message = "El estado en que esta es obligatorio")
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String estado;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
}
