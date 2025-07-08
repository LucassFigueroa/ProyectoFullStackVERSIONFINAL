package com.fitlife.pago.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "Pago")
public class pagomodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del pago", example = "1")
    private Long id;

    @NotNull(message = "El idCliente es obligatorio")
    @Schema(description = "ID del cliente que realizó el pago", example = "123")
    private Long idCliente;

    @NotNull(message = "El monto es obligatorio")
    @Schema(description = "Monto total del pago", example = "15000")
    private Integer monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Schema(description = "Método de pago utilizado", example = "Transferencia")
    private String metodoPago;

    @NotNull(message = "La fecha de pago es obligatoria")
    @Schema(description = "Fecha en que se realizó el pago", example = "2025-07-06")
    private LocalDate fechaPago;

    @NotBlank(message = "El estado es obligatorio")
    @Schema(description = "Estado actual del pago", example = "Pagado")
    private String estado;
}
