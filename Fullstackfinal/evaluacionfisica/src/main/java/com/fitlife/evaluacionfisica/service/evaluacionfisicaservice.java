package com.fitlife.evaluacionfisica.service;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.repository.evaluacionfisicarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class evaluacionfisicaservice {

    @Autowired
    private evaluacionfisicarepository evaluacionfisicarepository;

    public evaluacionfisica save(evaluacionfisica evaluacion) {
        return evaluacionfisicarepository.save(evaluacion);
    }

    public List<evaluacionfisica> getAll() {
        return evaluacionfisicarepository.findAll();
    }

    public evaluacionfisica getById(Long id) {
        return evaluacionfisicarepository.findById(id).orElse(null);
    }

    public evaluacionfisica update(Long id, evaluacionfisica details) {
        return evaluacionfisicarepository.findById(id).map(evaluacion -> {
            evaluacion.setUsuarioid(details.getUsuarioid());
            evaluacion.setPeso(details.getPeso());
            evaluacion.setAltura(details.getAltura());
            evaluacion.setImc(details.getImc());
            evaluacion.setFechaevaluacion(details.getFechaevaluacion());
            evaluacion.setObservaciones(details.getObservaciones());
            return evaluacionfisicarepository.save(evaluacion);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (evaluacionfisicarepository.existsById(id)) {
            evaluacionfisicarepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<evaluacionfisica> getByUsuarioId(Long usuarioid) {
        return evaluacionfisicarepository.findByUsuarioid(usuarioid);
    }

    public List<evaluacionfisica> getByFechaEvaluacionBetween(LocalDate desde, LocalDate hasta) {
        return evaluacionfisicarepository.findByFechaevaluacionBetween(desde, hasta);
    }
}
