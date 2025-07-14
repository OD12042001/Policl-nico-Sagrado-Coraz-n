package com.webapplication.PoliclinicoSagradoCorazon.config;

import java.io.IOException;

import com.webapplication.PoliclinicoSagradoCorazon.dto.AdministradorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.service.AdministradorService;
import com.webapplication.PoliclinicoSagradoCorazon.service.PacienteService;
import com.webapplication.PoliclinicoSagradoCorazon.service.RecepcionistaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private RecepcionistaService recepcionistaService;
    @Autowired
    private AdministradorService administradorService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String dni = authentication.getName();

        // Solo carga paciente si el rol es PACIENTE (opcional)
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        // Según rol, cargar datos y guardar en sesión
        HttpSession session = request.getSession(true);
        System.out.println("🔐 Login exitoso - Rol: " + rol + ", DNI: " + dni);
        switch (rol) {
            case "PACIENTE":
                PacienteDTO paciente = pacienteService.obtenerPorDni(dni);
                session.setAttribute("usuarioActual", paciente);
                System.out.println("👤 Paciente guardado en sesión: " + paciente.getNombre());
                response.sendRedirect("/portalPaciente");
                break;
            case "RECEPCIONISTA":
                RecepcionistaDTO recepcionista = recepcionistaService.obtenerPorDni(dni);
                session.setAttribute("usuarioActual", recepcionista);
                response.sendRedirect("/portalRecepcionista");
                break;
            case "ADMINISTRADOR":
                AdministradorDTO administrador = administradorService.obtenerPorDni(dni);
                session.setAttribute("usuarioActual", administrador);
                response.sendRedirect("/portalAdministrador");
                break;
            default:
                response.sendRedirect("/");
                break;
        }
    }
}
