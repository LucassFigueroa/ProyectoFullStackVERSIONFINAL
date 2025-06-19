package com.fitlife.evaluacionfisica.repository;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface evaluacionfisicarepository extends JpaRepository<evaluacionfisica, Long> {
}
