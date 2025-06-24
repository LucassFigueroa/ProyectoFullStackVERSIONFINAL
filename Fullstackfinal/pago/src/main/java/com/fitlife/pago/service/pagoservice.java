package com.fitlife.pago.service;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.repository.pagorepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class pagoservice {

    private final pagorepository pagoRepository;

    public pagoservice(pagorepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public pagomodel crearPago(pagomodel pago) {
        return pagoRepository.save(pago);
    }

    public List<pagomodel> listarPagos() {
        return pagoRepository.findAll();
    }

    public pagomodel obtenerPorId(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public pagomodel actualizarPago(Long id, pagomodel pago) {
        if (!pagoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el ID " + id);
        }
        pago.setId(id);
        return pagoRepository.save(pago);
    }

    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el ID " + id);
        }
        pagoRepository.deleteById(id);
    }

    // Buscar por estado
    public List<pagomodel> buscarPorEstado(String estado) {
        return pagoRepository.findByEstadoIgnoreCase(estado);
    }

    // Buscar por rango de fecha de pago
    public List<pagomodel> buscarPorRangoFecha(LocalDate desde, LocalDate hasta) {
        return pagoRepository.findByFechaPagoBetween(desde, hasta);
    }

    // Buscar por método de pago
    public List<pagomodel> buscarPorMetodo(String metodo) {
        return pagoRepository.findByMetodoPagoIgnoreCase(metodo);
    }

    // Buscar por monto mayor que
    public List<pagomodel> buscarPorMontoMayor(Double monto) {
        return pagoRepository.findByMontoGreaterThan(monto);
    }

    // Buscar por monto menor que
    public List<pagomodel> buscarPorMontoMenor(Double monto) {
        return pagoRepository.findByMontoLessThan(monto);
    }
}
