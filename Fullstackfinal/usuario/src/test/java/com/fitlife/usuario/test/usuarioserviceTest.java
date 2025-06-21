package com.fitlife.usuario.test;

import com.fitlife.usuario.model.usuariomodel;
import com.fitlife.usuario.repository.usuariorepository;
import com.fitlife.usuario.service.usuarioservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class usuarioserviceTest {

    @Mock
    private usuariorepository usuariorepository;

    @InjectMocks
    private usuarioservice usuarioservice;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_EncriptaContrasena() {
        usuariomodel user = new usuariomodel(null, "AdminMaster", "admin@fitlife.com", "admin123", "ADMIN");

        when(usuariorepository.save(any(usuariomodel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        usuariomodel saved = usuarioservice.register(user);

        assertNotNull(saved);
        assertNotEquals("admin123", saved.getContrasena());
        assertTrue(passwordEncoder.matches("admin123", saved.getContrasena()));

        verify(usuariorepository, times(1)).save(any(usuariomodel.class));
    }

    @Test
    void testLogin_Correcto() {
        String rawPassword = "admin123";
        String hashed = passwordEncoder.encode(rawPassword);
        usuariomodel user = new usuariomodel(1L, "AdminMaster", "admin@fitlife.com", hashed, "ADMIN");

        when(usuariorepository.findByEmail("admin@fitlife.com")).thenReturn(user);

        usuariomodel logged = usuarioservice.login("admin@fitlife.com", rawPassword);

        assertNotNull(logged);
        assertEquals("admin@fitlife.com", logged.getEmail());
    }

    @Test
    void testLogin_EmailInexistente() {
        when(usuariorepository.findByEmail("noexiste@fitlife.com")).thenReturn(null);

        usuariomodel logged = usuarioservice.login("noexiste@fitlife.com", "whatever");

        assertNull(logged);
    }

    @Test
    void testLogin_PasswordIncorrecta() {
        String hashed = passwordEncoder.encode("admin123");
        usuariomodel user = new usuariomodel(1L, "AdminMaster", "admin@fitlife.com", hashed, "ADMIN");

        when(usuariorepository.findByEmail("admin@fitlife.com")).thenReturn(user);

        usuariomodel logged = usuarioservice.login("admin@fitlife.com", "wrongpass");

        assertNull(logged);
    }
}
