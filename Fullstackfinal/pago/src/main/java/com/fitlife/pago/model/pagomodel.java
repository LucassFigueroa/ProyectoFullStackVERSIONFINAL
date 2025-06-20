package com.fitlife.pago.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class pagomodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El idCliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El monto es obligatorio")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;
 
}
