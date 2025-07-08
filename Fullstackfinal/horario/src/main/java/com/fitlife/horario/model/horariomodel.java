package com.fitlife.horario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "horarios")
@Schema(description = "Modelo que representa un horario asignado a un entrenador")
public class horariomodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del horario", example = "1")
    private Long id;

    @NotNull(message = "El ID del entrenador es obligatorio.")
    @Schema(description = "ID del entrenador asignado al horario", example = "12")
    private Long entrenadorId;

    @NotNull(message = "La fecha y hora de inicio es obligatoria.")
    @Schema(description = "Fecha y hora de inicio del horario", example = "2025-07-10T08:00:00")
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin es obligatoria.")
    @Schema(description = "Fecha y hora de fin del horario", example = "2025-07-10T10:00:00")
    private LocalDateTime fechaHoraFin;
}
