package com.fitlife.reporte.service;

import com.fitlife.reporte.model.reportemodel;
import com.fitlife.reporte.repository.reporterepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public reportemodel getById(Long id) {
        return reporterepository.findById(id).orElse(null);
    }

    public reportemodel update(Long id, reportemodel details) {
        return reporterepository.findById(id).map(reporte -> {
            reporte.setTitulo(details.getTitulo());
            reporte.setDescripcion(details.getDescripcion());
            return reporterepository.save(reporte);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (reporterepository.existsById(id)) {
            reporterepository.deleteById(id);
            return true;
        }
        return false;
    }
}
