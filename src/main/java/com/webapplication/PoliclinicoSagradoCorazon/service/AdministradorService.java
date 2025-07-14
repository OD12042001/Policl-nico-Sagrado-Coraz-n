package com.webapplication.PoliclinicoSagradoCorazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dao.AdministradorDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.AdministradorDTO;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorDAO administradorDAO;

    public AdministradorDTO obtenerPorDni(String dni) {
        return administradorDAO.buscarPorDni(dni);
    }
}
