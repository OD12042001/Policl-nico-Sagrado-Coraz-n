package com.webapplication.PoliclinicoSagradoCorazon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webapplication.PoliclinicoSagradoCorazon.service.PacienteService;

@RestController
public class PacienteRestController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/validarDni")
    public boolean validarDni(@RequestParam String dni) {
        // Retorna true si ya existe, false si no
        return pacienteService.existeDni(dni);
    }
}
