package com.fitlife.acceso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accesos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class accesomodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(max = 20, message = "El nombre de usuario no puede superar los 20 caracteres")
    @Column(length = 20, nullable = false, unique = true)
    private String nombreUsuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 20, message = "La contraseña no puede superar los 20 caracteres")
    @Column(length = 20, nullable = false)
    private String contrasena;
}
