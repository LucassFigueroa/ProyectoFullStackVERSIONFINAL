package com.fitlife.reporte.service;

import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.repository.reporterepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class reporteservice {

    @Autowired
    private reporterepository reporterepository;

    public reportemodel save(reportemodel reporte) {
        return reporterepository.save(reporte);
    }

    public List<reportemodel> getAll() {
        return reporterepository.findAll();
    }

    public Page<reportemodel> getAll(Pageable pageable) {
        return reporterepository.findAll(pageable);
    }

    public reportemodel getById(Long id) {
        return reporterepository.findById(id).orElse(null);
    }

    public List<reportemodel> getByTipo(String tipo) {
        return reporterepository.findByTipo(tipo);
    }

    public List<reportemodel> getByFechaCreacionBetween(LocalDateTime desde, LocalDateTime hasta) {
        return reporterepository.findByFechaCreacionBetween(desde, hasta);
    }

    @Transactional
    public reportemodel update(Long id, reportemodel details) {
        Optional<reportemodel> optional = reporterepository.findById(id);
        if (optional.isPresent()) {
            reportemodel reporte = optional.get();
            reporte.setTitulo(details.getTitulo());
            reporte.setDescripcion(details.getDescripcion());
            reporte.setTipo(details.getTipo());
            return reporterepository.save(reporte);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (reporterepository.existsById(id)) {
            reporterepository.deleteById(id);
            return true;
        }
        return false;
    }
}
