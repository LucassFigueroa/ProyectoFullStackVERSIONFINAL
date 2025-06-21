package com.fitlife.pago.service;

import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.repository.pagorepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
public class pagoservice {

    private final pagorepository pagoRepository;

    // Constructor para inyección de dependencia
    public pagoservice(pagorepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    // Crea un pago nuevo
    public pagomodel guardarPago(pagomodel pago) {
        return pagoRepository.save(pago);
    }

    // Devuelve todos los pagos
    public List<pagomodel> obtenerPagos() {
        return pagoRepository.findAll();
    }

    // Busca pago por ID
    public Optional<pagomodel> obtenerPorId(Long id) {
        return pagoRepository.findById(id);
    }

    // Actualiza pago existente
    public pagomodel actualizarPago(Long id, pagomodel pago) {
        if (!pagoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pago con ID " + id + " no existe");
        }
        pago.setId(id);
        return pagoRepository.save(pago);
    }

    // Elimina pago por ID
    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pago con ID " + id + " no existe");
        }
        pagoRepository.deleteById(id);
    }

    // Filtros:
    public List<pagomodel> obtenerPagosPorCliente(Long idCliente) {
        return pagoRepository.findByIdCliente(idCliente);
    }

    public List<pagomodel> obtenerPagosPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    public List<pagomodel> obtenerPagosPorClienteYEstado(Long idCliente, String estado) {
        return pagoRepository.findByIdClienteAndEstado(idCliente, estado);
    }
}
