package com.fitlife.pago.service;

import org.springframework.stereotype.Service;
import com.fitlife.pago.model.pagomodel;
import com.fitlife.pago.repository.pagorepository;

import java.util.List;

@Service
public class pagoservice {
   private final pagorepository pagoRepository;

     public pagoservice(pagorepository pagoRepository){
        this.pagoRepository = pagoRepository;
    }

    public List<pagomodel> obtenerPagos(){
        return pagoRepository.findAll();
    }

    public pagomodel guardarPago(pagomodel pago){
        return pagoRepository.save(pago);
    }

    public List<pagomodel> obtenerPagosPorCliente(Long idCliente){
        return pagoRepository.findByIdCliente(idCliente);
    }

}
  
