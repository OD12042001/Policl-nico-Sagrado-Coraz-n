package com.webapplication.PoliclinicoSagradoCorazon.service;

import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dto.AdministradorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.repository.AdministradorConsultaRepository;

@Service
public class AdministradorService {

    private final AdministradorConsultaRepository administradorConsultaRepository;

    public AdministradorService(AdministradorConsultaRepository administradorConsultaRepository) {
        this.administradorConsultaRepository = administradorConsultaRepository;
    }

    public AdministradorDTO obtenerPorDni(String dni) {
        return administradorConsultaRepository.buscarPorDni(dni);
    }
}
