package com.webapplication.PoliclinicoSagradoCorazon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;
import com.webapplication.PoliclinicoSagradoCorazon.repository.EspecialidadConsultaRepository;

@Service
public class EspecialidadService {

    private final EspecialidadConsultaRepository especialidadConsultaRepository;
    

    public EspecialidadService(EspecialidadConsultaRepository especialidadConsultaRepository) {
        this.especialidadConsultaRepository = especialidadConsultaRepository;
    }


    public List<Especialidad> listarTodas(){
        return especialidadConsultaRepository.listarTodasidNombre();
    }
}
