package com.fitlife.pago.service;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.repository.pagorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class pagoservice {

    @Autowired
    private pagorepository pagorepository;
    
    public void setRepository(pagorepository pagorepository) {
        this.pagorepository = pagorepository;
    }

    public pagomodel crearPago(pagomodel pago) {
        return pagorepository.save(pago);
    }

    public List<pagomodel> listarPagos() {
        return pagorepository.findAll();
    }

    public pagomodel obtenerPorId(Long id) {
        return pagorepository.findById(id).orElse(null);
    }

    public pagomodel actualizarPago(Long id, pagomodel pago) {
        if (!pagorepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el ID " + id);
        }
        pago.setId(id);
        return pagorepository.save(pago);
    }

    public void eliminarPago(Long id) {
        if (!pagorepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el ID " + id);
        }
        pagorepository.deleteById(id);
    }

    public List<pagomodel> buscarPorEstado(String estado) {
        return pagorepository.findByEstadoIgnoreCase(estado);
    }

    public List<pagomodel> buscarPorRangoFecha(LocalDate desde, LocalDate hasta) {
        return pagorepository.findByFechaPagoBetween(desde, hasta);
    }

    public List<pagomodel> buscarPorMontoMayor(Integer monto) {
        return pagorepository.findByMontoGreaterThan(monto);
    }

    public List<pagomodel> buscarPorMontoMenor(Integer monto) {
        return pagorepository.findByMontoLessThan(monto);
    }

    public List<pagomodel> buscarPorMetodo(String metodoPago) {
        return pagorepository.findByMetodoPagoIgnoreCase(metodoPago);
    }
}
