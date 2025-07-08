package com.fitlife.membresia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "membresia")
@Schema(description = "Modelo que representa una membresía de usuario")
public class membresiamodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la membresía", example = "1")
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Schema(description = "ID del usuario asociado a la membresía", example = "10")
    private Long usuarioId;

    @NotBlank(message = "El tipo de membresía es obligatorio")
    @Schema(description = "Tipo de membresía contratada", example = "Mensual")
    private String tipo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha de inicio de la membresía", example = "2025-07-08")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Schema(description = "Fecha de finalización de la membresía", example = "2025-08-08")
    private LocalDate fechaFin;

    @NotBlank(message = "El estado es obligatorio")
    @Schema(description = "Estado actual de la membresía (Activa, Inactiva, Cancelada)", example = "Activa")
    private String estado;
}
