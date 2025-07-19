package com.webapplication.PoliclinicoSagradoCorazon.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail = "notificaciones@clinica.com";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailCancelacion(String toEmail, String nombrePaciente,
            String fechaHoraCita, String motivo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Cancelación de tu cita médica");

        String texto = String.format(
                "Hola %s,\n\n" +
                        "Lamentamos informarte que tu cita programada para el %s ha sido cancelada.\n" +
                        "Motivo: %s\n\n" +
                        "Por favor, inicia sesión en nuestro portal para reagendar tu cita.\n\n" +
                        "Atentamente,\nEl equipo de la clínica",
                nombrePaciente, fechaHoraCita, motivo);

        message.setText(texto);

        mailSender.send(message);
    }

    @Async
    public void enviarEmailConfirmacionCita(String toEmail, String nombrePaciente,
            String nombreDoctor, LocalDate fecha,
            LocalTime hora, BigDecimal precio,
            String metodoPago) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Confirmación de cita médica");

            String texto = String.format(
                "Hola %s,\n\n" +
                "Tu cita médica ha sido confirmada con los siguientes detalles:\n\n" +
                "Doctor: %s\n" +
                "Fecha: %s\n" +
                "Hora: %s\n" +
                "Método de pago: %s\n" +
                "Monto pagado: %s\n\n" +
                "Por favor, presenta este correo como comprobante el día de tu cita.\n\n" +
                "Atentamente,\nEl equipo de la clínica",
                nombrePaciente, 
                nombreDoctor, 
                fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                hora.format(DateTimeFormatter.ofPattern("HH:mm")), 
                metodoPago, 
                precio.toString());

            message.setText(texto);
            mailSender.send(message);
            
        } catch (Exception e) {
            
        }
    }
}
