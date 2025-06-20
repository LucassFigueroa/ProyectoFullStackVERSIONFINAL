package com.fitlife.inventario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private Long id;

    @NotBlank(message = "El nombre del artículo no puede estar vacío")
    @Size(max = 20, message = "El nombre del artículo no puede superar los 20 caracteres")
    @Column(name = "nombre_articulo", nullable = false)
    private String nombreArticulo;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidad;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
    
}
