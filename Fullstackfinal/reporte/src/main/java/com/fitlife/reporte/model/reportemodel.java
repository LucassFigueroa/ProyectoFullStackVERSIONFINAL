package com.fitlife.reporte.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class reportemodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 50, message = "El título no puede exceder los 50 caracteres")
    @Column(length = 50, nullable = false)
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    @Column(length = 255, nullable = false)
    private String descripcion;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    @Size(max = 30, message = "El tipo no puede exceder los 30 caracteres")
    @Column(length = 30)
    private String tipo;
}
