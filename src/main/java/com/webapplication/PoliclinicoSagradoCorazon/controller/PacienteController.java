package com.webapplication.PoliclinicoSagradoCorazon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.service.PacienteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/registrarPaciente")
    public String registrarPaciente(@ModelAttribute PacienteDTO dto, HttpServletRequest request) {

        // Validar que contraseña y confirmación coincidan (puedes hacerlo aquí o en
        // frontend)
        // ...
        System.out.println("📌 Entró al método registrarPaciente()");
        System.out.println("DNI recibido: " + dto.getDni());
        // Guardar paciente y usuario
        pacienteService.registrarPaciente(dto);

        // Login automático después de registro
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getDni(),
                    dto.getContraseña());

            Authentication auth = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);

            // Crear sesión Http si quieres
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
            PacienteDTO paciente = pacienteService.obtenerPorDni(dto.getDni());
            session.setAttribute("usuarioActual", paciente);

        } catch (AuthenticationException e) {
            e.printStackTrace();
            // Si algo falla, redirige a login con error
            return "redirect:/login?errorRegistro";
        }

        // Redirigir a portal paciente
        return "redirect:/portalPaciente";
    }

    @GetMapping("/registrarPaciente")
    public String mostrarRegistro() {
        return "login"; // o redireccionas a /login
    }

    @GetMapping("/paciente/editarPerfil/{dni}")
    public String mostrarFormularioEdicion(@PathVariable("dni") String dni, Model model) {
        PacienteDTO paciente = pacienteService.obtenerPorDni(dni);
        model.addAttribute("paciente", paciente);
        
        System.out.println("hola como estas gaaaaa ");

        return "paciente-dashboard/editarPerfil"; // crea este HTML
    }

    @PostMapping("/paciente/actualizarPerfil")
    public String actualizarPerfil(@ModelAttribute PacienteDTO paciente, HttpSession session) {

        PacienteDTO pacienteactual = (PacienteDTO) session.getAttribute("usuarioActual");
        pacienteService.actualizarDatosContacto(paciente,pacienteactual);
        PacienteDTO actualizado = pacienteService.obtenerPorDni(pacienteactual.getDni());
        session.setAttribute("usuarioActual", actualizado);
        return "redirect:/portalPaciente";
    }

}
