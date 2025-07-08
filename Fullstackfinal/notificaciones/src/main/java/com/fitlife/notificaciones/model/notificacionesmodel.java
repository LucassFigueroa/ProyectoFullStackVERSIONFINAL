package com.fitlife.notificaciones.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notificaciones")
@Schema(description = "Modelo que representa una notificación enviada a un usuario")
public class notificacionesmodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la notificación", example = "1")
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Schema(description = "ID del usuario que recibe la notificación", example = "1001")
    private Long usuarioId;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Contenido del mensaje de la notificación", example = "Tu clase de yoga comienza en 30 minutos.")
    private String mensaje;

    @NotNull(message = "La fecha de envío es obligatoria")
    @Schema(description = "Fecha y hora de envío de la notificación", example = "2025-07-08T15:30:00")
    private LocalDateTime fechaEnvio;

    @NotBlank(message = "El estado es obligatorio")
    @Column(length = 20, nullable = false)
    @Schema(description = "Estado de la notificación (Leída o No Leída)", example = "No Leída")
    private String estado;
}
