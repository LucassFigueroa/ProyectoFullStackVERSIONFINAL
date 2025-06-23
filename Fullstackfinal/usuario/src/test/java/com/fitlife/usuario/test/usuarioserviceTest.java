package com.fitlife.usuario.test;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.repository.usuariorepository;
import com.fitlife.usuario.service.usuarioservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 👈 IMPORTANTE: habilita Mockito correctamente
public class usuarioserviceTest {

    @Mock
    private usuariorepository usuariorepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private usuarioservice usuarioservice;

    @BeforeEach
    void setUp() {
        usuarioservice = new usuarioservice(usuariorepository, passwordEncoder);
    }

    @Test
void testLogin_Correcto() {
    String rawPassword = "admin123";
    String hashed = "HASHED";
    // Orden correcto: id, email, contrasena, nombre, rol
    usuariomodel user = new usuariomodel(1L, "admin@fitlife.com", hashed, "AdminMaster", "ADMIN");

    when(usuariorepository.findByEmail("admin@fitlife.com")).thenReturn(user);
    when(passwordEncoder.matches(rawPassword, hashed)).thenReturn(true);

    usuariomodel logged = usuarioservice.login("admin@fitlife.com", rawPassword);

    assertNotNull(logged);
    assertEquals("admin@fitlife.com", logged.getEmail());
}

@Test
void testRegister_EncriptaContrasena() {
    usuariomodel user = new usuariomodel(null, "admin@fitlife.com", "admin123", "AdminMaster", "ADMIN");

    when(passwordEncoder.encode("admin123")).thenReturn("HASHED");
    when(usuariorepository.save(any(usuariomodel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    usuariomodel saved = usuarioservice.register(user);

    assertNotNull(saved);
    assertEquals("HASHED", saved.getContrasena());
    verify(passwordEncoder).encode("admin123");
    verify(usuariorepository).save(any(usuariomodel.class));
}

@Test
void testLogin_PasswordIncorrecta() {
    String rawPassword = "admin123";
    String hashed = "HASHED";
    usuariomodel user = new usuariomodel(1L, "admin@fitlife.com", hashed, "AdminMaster", "ADMIN");

    when(usuariorepository.findByEmail("admin@fitlife.com")).thenReturn(user);
    when(passwordEncoder.matches(rawPassword, hashed)).thenReturn(false);

    usuariomodel logged = usuarioservice.login("admin@fitlife.com", rawPassword);

    assertNull(logged);
}

    @Test
    void testLogin_EmailInexistente() {
        when(usuariorepository.findByEmail("x@x.com")).thenReturn(null);
        usuariomodel logged = usuarioservice.login("x@x.com", "pass");
        assertNull(logged);
    }
}
