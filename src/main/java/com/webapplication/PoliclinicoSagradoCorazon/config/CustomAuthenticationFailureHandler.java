package com.webapplication.PoliclinicoSagradoCorazon.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        HttpSession session = request.getSession();
        String errorMessage;

        // Personaliza mensajes según la excepción (opcional)
        if (exception.getMessage().contains("Bad credentials")) {
            errorMessage = "Usuario o Contraseña incorrecto.";
        } else if (exception.getMessage().contains("User not found")) {
            errorMessage = "Usuario no registrado.";
        } else {
            errorMessage = "Error de autenticación. Intente nuevamente.";
        }

        session.setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login?error");
    }
}