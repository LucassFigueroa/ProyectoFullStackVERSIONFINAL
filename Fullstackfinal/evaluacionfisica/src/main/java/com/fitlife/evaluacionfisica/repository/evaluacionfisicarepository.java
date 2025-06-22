package com.fitlife.evaluacionfisica.repository;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface evaluacionfisicarepository extends JpaRepository<evaluacionfisica, Long> {

    List<evaluacionfisica> findByUsuarioid(Long usuarioid);

    List<evaluacionfisica> findByFechaevaluacionBetween(LocalDate desde, LocalDate hasta);
}
