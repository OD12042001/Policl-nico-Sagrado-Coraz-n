package com.webapplication.PoliclinicoSagradoCorazon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.service.RecepcionistaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @GetMapping("/administrador/recepcionista/nuevo")
    public String nuevoRecepcionista(Model model,HttpSession session) {
        model.addAttribute("recepcionista", new RecepcionistaDTO());

        session.setAttribute("recepcionista1", new RecepcionistaDTO());
        
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/formularioRecepcionista";
    }

    @GetMapping("/administrador/recepcionista/modificar/{id}")
    public String modificarRecepcionista(@PathVariable int id, Model model,HttpSession session) {
        RecepcionistaDTO recepcionista = recepcionistaService.obtenerPorId(id);
        model.addAttribute("recepcionista", recepcionista);
        
        session.setAttribute("recepcionista1", recepcionista);

        return "redirect:/portalAdministrador?contenido=administrador-dashboard/formularioRecepcionista";
    }

    @PostMapping("/administrador/recepcionista/guardar")
    public String guardarRecepcionista(@ModelAttribute RecepcionistaDTO recepcionista) {
        if (recepcionista.getId() > 0) {
            recepcionistaService.actualizar(recepcionista);
        } else {
            System.out.println("aqui estoyyyy 111111111111111111111111");
            recepcionistaService.insertar(recepcionista);
        }
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/Recepcionistas";
    }

    @GetMapping("/administrador/recepcionista/activar/{dni}")
    public String activarRecepcionista(@PathVariable String dni) {
        recepcionistaService.activar(dni);
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/Recepcionistas";
    }

    @GetMapping("/administrador/recepcionista/desactivar/{dni}")
    public String desactivarRecepcionista(@PathVariable String dni) {
        recepcionistaService.desactivar(dni);
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/Recepcionistas";
    }

}
