package com.fitlife.evaluacionfisica.service;

import com.fitlife.evaluacionfisica.model.evaluacionfisica;
import com.fitlife.evaluacionfisica.repository.evaluacionfisicarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class evaluacionfisicaservice {

    @Autowired
    private evaluacionfisicarepository evaluacionfisicarepository;

    public evaluacionfisica saveEvaluacion(evaluacionfisica evaluacion) {
        return evaluacionfisicarepository.save(evaluacion);
    }

    public List<evaluacionfisica> getAllEvaluaciones() {
        return evaluacionfisicarepository.findAll();
    }

    public evaluacionfisica getEvaluacionById(Long id) {
        return evaluacionfisicarepository.findById(id).orElse(null);
    }

    public evaluacionfisica updateEvaluacion(Long id, evaluacionfisica details) {
        return evaluacionfisicarepository.findById(id).map(e -> {
            e.setClienteNombre(details.getClienteNombre());
            e.setPeso(details.getPeso());
            e.setAltura(details.getAltura());
            e.setImc(details.getImc());
            e.setEvaluador(details.getEvaluador());
            return evaluacionfisicarepository.save(e);
        }).orElse(null);
    }

    public boolean deleteEvaluacion(Long id) {
        if (evaluacionfisicarepository.existsById(id)) {
            evaluacionfisicarepository.deleteById(id);
            return true;
        }
        return false;
    }
}
