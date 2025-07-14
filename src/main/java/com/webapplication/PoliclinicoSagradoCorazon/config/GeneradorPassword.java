package com.webapplication.PoliclinicoSagradoCorazon.config;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneradorPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passEncriptada = encoder.encode("12345678");
        System.out.println("Contraseña encriptada: " + passEncriptada);
    }
}
