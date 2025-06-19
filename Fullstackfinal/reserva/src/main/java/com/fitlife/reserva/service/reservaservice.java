package com.fitlife.reserva.service;

import com.fitlife.reserva.model.reservamodel;
import com.fitlife.reserva.repository.reservarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class reservaservice {

    @Autowired
    private reservarepository reservarepository;

    public reservamodel save(reservamodel reserva) {
        return reservarepository.save(reserva);
    }

    public List<reservamodel> getAll() {
        return reservarepository.findAll();
    }

    public reservamodel getById(Long id) {
        return reservarepository.findById(id).orElse(null);
    }

    public reservamodel update(Long id, reservamodel details) {
        return reservarepository.findById(id).map(reserva -> {
            reserva.setUsuarioid(details.getUsuarioid());
            reserva.setClaseid(details.getClaseid());
            reserva.setFecha(details.getFecha());
            reserva.setHora(details.getHora());
            return reservarepository.save(reserva);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (reservarepository.existsById(id)) {
            reservarepository.deleteById(id);
            return true;
        }
        return false;
    }
}
