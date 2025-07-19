package com.webapplication.PoliclinicoSagradoCorazon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Puedes personalizar el mensaje de error según el código de estado
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        
        if (statusCode != null) {
            if (statusCode == 404) {
                model.addAttribute("errorMessage", "Página no encontrada");
            } else if (statusCode == 500) {
                model.addAttribute("errorMessage", "Error interno del servidor");
            } else {
                model.addAttribute("errorMessage", "Ocurrió un error inesperado");
            }
        }
        return "error"; // Nombre de tu plantilla de error
    }
}
