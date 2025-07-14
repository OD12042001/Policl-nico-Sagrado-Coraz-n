package com.webapplication.PoliclinicoSagradoCorazon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // login.html en /templates
    }

    @GetMapping("/index")
    public String mostrarIndex() {
        return "index"; // index.html en /templates
    }
}
