package com.fitlife.reserva.service;

import com.fitlife.reserva.model.reserva;
import com.fitlife.reserva.repository.reservarepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class reservaservice {

    private final reservarepository reservarepository;

    public reserva saveReserva(reserva reserva) {
        boolean exists = reservarepository.existsByClienteNombreAndFechaAndHora(
                reserva.getClienteNombre(), reserva.getFecha(), reserva.getHora());
        if (exists) {
            throw new IllegalArgumentException("Ya tienes una reserva en ese horario.");
        }
        return reservarepository.save(reserva);
    }

    public List<reserva> getAllReservas() {
        return reservarepository.findAll();
    }

    public Optional<reserva> getReservaById(Long id) {
        return reservarepository.findById(id);
    }

    public reserva updateReserva(Long id, reserva nueva) {
        return reservarepository.findById(id)
                .map(r -> {
                    r.setClienteNombre(nueva.getClienteNombre());
                    r.setFecha(nueva.getFecha());
                    r.setHora(nueva.getHora());
                    r.setEstado(nueva.getEstado());
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

    public List<reserva> getReservasByClienteNombre(String nombre) {
        return reservarepository.findByClienteNombre(nombre);
    }
}
