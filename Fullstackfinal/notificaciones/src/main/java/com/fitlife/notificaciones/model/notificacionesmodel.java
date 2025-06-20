package com.fitlife.notificaciones.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notificaciones")
public class notificacionesmodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @NotNull(message = "La fecha de envío es obligatoria")
    private LocalDateTime fechaEnvio;

    @NotBlank(message = "El estado es obligatorio")
    @Column(length = 20, nullable = false)
    private String estado;
}
