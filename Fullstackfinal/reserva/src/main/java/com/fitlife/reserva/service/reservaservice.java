package com.fitlife.reserva.service;

import com.fitlife.reserva.model.reserva;
import com.fitlife.reserva.repository.reservarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class reservaservice {

    @Autowired
    private reservarepository reservarepository;

    public reserva saveReserva(reserva reserva) {
        return reservarepository.save(reserva);
    }

    public List<reserva> getAllReservas() {
        return reservarepository.findAll();
    }

    public Optional<reserva> getReservaById(Long id) {
        return reservarepository.findById(id);
    }

    public reserva updateReserva(Long id, reserva details) {
        return reservarepository.findById(id).map(r -> {
            r.setClienteNombre(details.getClienteNombre());
            r.setFecha(details.getFecha());
            r.setHora(details.getHora());
            r.setEstado(details.getEstado());
            return reservarepository.save(r);
        }).orElse(null);
    }

    public boolean deleteReserva(Long id) {
        if (reservarepository.existsById(id)) {
            reservarepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<reserva> getReservasByClienteNombre(String clienteNombre) {
        return reservarepository.findByClienteNombre(clienteNombre);
    }
}
