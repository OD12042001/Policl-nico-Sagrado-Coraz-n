package com.webapplication.PoliclinicoSagradoCorazon.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;
import com.webapplication.PoliclinicoSagradoCorazon.model.Doctor;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;
import com.webapplication.PoliclinicoSagradoCorazon.model.Paciente1;
import com.webapplication.PoliclinicoSagradoCorazon.service.CitaService;
import com.webapplication.PoliclinicoSagradoCorazon.service.DoctorService;
import com.webapplication.PoliclinicoSagradoCorazon.service.EmailService;
import com.webapplication.PoliclinicoSagradoCorazon.service.HorarioService;
import com.webapplication.PoliclinicoSagradoCorazon.service.PacienteService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PagoController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/procesar-pago")
public String procesarPago(
        @RequestParam("precio") BigDecimal precio,
        @RequestParam("pacienteId") int pacienteId,
        @RequestParam("horario_id") int horarioId,
        @RequestParam("metodo") String metodo,
        HttpSession session, 
        RedirectAttributes redirectAttributes) {

    try {
        // Registrar cita y obtener los datos completos
        citaService.registrarCita(precio, pacienteId, horarioId, metodo);
        Paciente1 paciente = pacienteService.obtenerPorId(pacienteId);
        Horario horario = horarioService.obtenerPorId(horarioId);
        DoctorDTO doctor = doctorService.obtenerPorId(horario.getDoctor_id());

        // Enviar email de confirmación (manejado de forma asíncrona)
        try {
            emailService.enviarEmailConfirmacionCita(
                paciente.getCorreo(),
                paciente.getNombre(),
                doctor.getNombre(),
                horario.getFecha(),
                horario.getHora(),
                precio,
                metodo);
        } catch (Exception emailEx) {
            // Logear el error pero no interrumpir el flujo
            
        }

        redirectAttributes.addFlashAttribute("success", "Cita registrada exitosamente");
        
        // Redirigir según tipo de usuario
        Object usuarioActual = session.getAttribute("usuarioActual");
        if (usuarioActual instanceof PacienteDTO) {
            return "redirect:/portalPaciente?contenido=paciente-dashboard/MisCitas";
        } else if (usuarioActual instanceof RecepcionistaDTO) {
            return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/citastotales";
        }
        
        return "redirect:/confirmacion";

    } catch (Exception e) {
        
        redirectAttributes.addFlashAttribute("error", "Error al registrar la cita: " + e.getMessage());
        
        // Redirigir a la página anterior con parámetros para mostrar el error
        return "redirect:/agendar-cita?horario_id=" + horarioId + "&error=true";
    }
}

    @GetMapping("/confirmacion")
    public String mostrarConfirmacion() {
        return "confirmacion"; // esto carga confirmacion.html en templates/
    }

}
