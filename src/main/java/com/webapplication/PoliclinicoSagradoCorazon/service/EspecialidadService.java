package com.webapplication.PoliclinicoSagradoCorazon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dao.ListaDAO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;

@Service
public class EspecialidadService {

    @Autowired
    private ListaDAO listaDAO;

    public List<Especialidad> listarTodas(){
        return listaDAO.listarTodas();
    }
}
