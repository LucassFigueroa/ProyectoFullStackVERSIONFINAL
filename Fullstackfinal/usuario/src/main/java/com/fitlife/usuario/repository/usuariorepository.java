package com.fitlife.usuario.repository;

import com.fitlife.usuario.model.usuariomodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface usuariorepository extends JpaRepository<usuariomodel, Long> {
    boolean existsByEmail(String email);
    usuariomodel findByEmail(String email);
}
