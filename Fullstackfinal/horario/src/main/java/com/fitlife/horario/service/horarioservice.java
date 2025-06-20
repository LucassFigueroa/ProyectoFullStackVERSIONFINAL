package com.fitlife.horario.service;

import com.fitlife.horario.model.horariomodel;
import com.fitlife.horario.repository.horariorepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class horarioservice {

    
    private final horariorepository horarioRepository;

    public horarioservice(horariorepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    public horariomodel guardarHorario(horariomodel horario) {
        return horarioRepository.save(horario);
    }

    public List<horariomodel> listarHorarios() {
        return horarioRepository.findAll();
    }

    public Optional<horariomodel> obtenerPorId(Long id) {
        return horarioRepository.findById(id);
    }

    public horariomodel actualizarHorario(Long id, horariomodel horario) {
        horario.setId(id);
        return horarioRepository.save(horario);
    }

    public void eliminarHorario(Long id) {
        horarioRepository.deleteById(id);
    }
}
