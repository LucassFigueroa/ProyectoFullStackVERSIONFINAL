package com.fitlife.membresia.service;

import com.fitlife.membresia.model.membresiamodel;
import com.fitlife.membresia.repository.membresiarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class membresiaservice {

    @Autowired
    private membresiarepository membresiaRepository;

    public membresiamodel guardarMembresia(membresiamodel membresia) {
        return membresiaRepository.save(membresia);
    }

    public List<membresiamodel> obtenerTodas() {
        return membresiaRepository.findAll();
    }
}
