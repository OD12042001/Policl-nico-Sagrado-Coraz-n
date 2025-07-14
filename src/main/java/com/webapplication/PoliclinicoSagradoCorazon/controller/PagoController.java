package com.webapplication.PoliclinicoSagradoCorazon.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.service.CitaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PagoController {

    @Autowired
    private CitaService citaService;

    @PostMapping("/procesar-pago")
    public String procesarPago(
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("pacienteId") int pacienteId,
            @RequestParam("horario_id") int horarioId,
            @RequestParam("metodo") String metodo,
            HttpSession session) {

        // Registrar cita
        citaService.registrarCita(precio, pacienteId, horarioId, metodo);

        Object usuarioActual = session.getAttribute("usuarioActual");
        // Redirigir a una página de confirmación
        if (usuarioActual instanceof PacienteDTO paciente1){
            return "redirect:/portalPaciente?contenido=paciente-dashboard/MisCitas";
        }else if(usuarioActual instanceof RecepcionistaDTO recepcionista){
            return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/citastotales";
        }
        return "redirect:/error";
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacion() {
        return "confirmacion"; // esto carga confirmacion.html en templates/
    }

}
