package com.fitlife.horario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder; 
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
@Table(name = "horarios")
public class horariomodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del entrenador es obligatorio.")
    private Long entrenadorId;

    @NotNull(message = "La fecha y hora de inicio es obligatoria.")
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin es obligatoria.")
    private LocalDateTime fechaHoraFin;
}
