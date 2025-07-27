package com.webapplication.PoliclinicoSagradoCorazon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioSeleccionadoDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.Notificacion;
import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;
import com.webapplication.PoliclinicoSagradoCorazon.service.CitaService;
import com.webapplication.PoliclinicoSagradoCorazon.service.DoctorService;
import com.webapplication.PoliclinicoSagradoCorazon.service.HorarioService;
import com.webapplication.PoliclinicoSagradoCorazon.service.PacienteService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/confirmar-cita")
    public String confirmarCita() {
        // Aquí podrías guardar datos si quieres, o dejar vacío si no haces nada
        return "redirect:/pago"; // redirige a la ruta /pago
    }

    @GetMapping("/pago")
    public String mostrarPago(HttpSession session, Model model) {
        HorarioSeleccionadoDTO horario = (HorarioSeleccionadoDTO) session.getAttribute("horarioSeleccionado");
        PacienteDTO paciente = (PacienteDTO) session.getAttribute("pacienteActual");
        Object usuario = session.getAttribute("usuarioActual");

        if (horario != null)
            model.addAttribute("horarioSeleccionado", horario);
        if (paciente != null)
            model.addAttribute("pacienteActual", paciente);

        // ✅ Esto es lo que faltaba
        if (usuario instanceof PacienteDTO) {
            model.addAttribute("rolUsuario", "paciente");
        } else if (usuario instanceof RecepcionistaDTO) {
            model.addAttribute("rolUsuario", "recepcionista");
        } else {
            model.addAttribute("rolUsuario", "otro");
        }

        return "pago";
    }

    @PostMapping("/confirmar-dni")
    public String confirmardni(@RequestParam("dni") String dni, HttpSession session) {

        PacienteDTO pacienteDTO = pacienteService.obtenerPorDni(dni);
        if (pacienteDTO == null) {
            session.setAttribute("errorDni", "El paciente con DNI " + dni + " no existe.");
            return "redirect:/formulario-dni"; // o la página donde ingresó el DNI
        }
        session.setAttribute("pacienteActual", pacienteDTO);
        return "redirect:/registro-dni"; // redirige a la ruta /pago
    }

    @SuppressWarnings("null")
    @GetMapping("/registro-dni")
    public String mostrarFormularioCita(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        HorarioSeleccionadoDTO horario = (HorarioSeleccionadoDTO) session.getAttribute("horarioSeleccionado");

        if (horario != null) {
            model.addAttribute("horarioSeleccionado", horario);
        }
        PacienteDTO paciente = (PacienteDTO) session.getAttribute("pacienteActual");
        if (paciente != null)
            model.addAttribute("pacienteActual", paciente);
        Object usuarioActual = session.getAttribute("usuarioActual");
        if (citaService.verificarSiExisteCitaEnFechaHora(paciente.getId(), horario.getFecha(), horario.getHora())) {
            redirectAttributes.addFlashAttribute("notificacion", new Notificacion(
                    "Error en agendamiento",
                    "El paciente ya cuenta con una cita programada para esta misma fecha y hora.",
                    "error"));
            return "redirect:/horario";
        } else {
            return "formulario-cita";
        }
    }

    @GetMapping("/formulario-dni")
    public String mostrarFormularioDni(HttpSession session, Model model) {
        HorarioSeleccionadoDTO horario = (HorarioSeleccionadoDTO) session.getAttribute("horarioSeleccionado");
        if (horario != null) {
            model.addAttribute("horarioSeleccionado", horario);
        }
        model.addAttribute("errorDni", session.getAttribute("errorDni"));
        session.removeAttribute("errorDni"); // evita que quede en sesión después
        return "formulario-dni"; // o como se llame tu HTML
    }

    @GetMapping("/paciente/cita/cancelar/{id}")
    public String cancelarCita(@PathVariable("id") int citaId, RedirectAttributes redirectAttributes) {
        citaService.cancelarCitaInteligente(citaId);
        /*
         * try {
         * // Cancelar la cita y obtener datos del paciente
         * 
         * Cita cita = citaService.obtenerPorId(citaId);
         * Paciente1 paciente =pacienteService.obtenerPorId(cita.getPaciente_id());
         * Horario horario = horarioService.obtenerPorId(cita.getHorario_id());
         * // Obtener datos para el email
         * 
         * String fechaHora = horario.getFecha().toString() + " " +
         * horario.getHora().toString();
         * 
         * // Enviar email de cancelación
         * emailService.enviarEmailCancelacion(
         * paciente.getCorreo(),
         * paciente.getNombre(),
         * fechaHora,
         * "Cancelación de la cita del paciente");
         * 
         * redirectAttributes.addFlashAttribute("success",
         * "Cita cancelada y notificación enviada");
         * } catch (Exception e) {
         * redirectAttributes.addFlashAttribute("error", "Error al cancelar la cita: " +
         * e.getMessage());
         * }
         */
        return "redirect:/portalPaciente?contenido=paciente-dashboard/MisCitas";
    }

    @GetMapping("/recepcionista/cita/cancelar/{id}")
    public String cancelarCitaPaciente(@PathVariable("id") int citaId) {
        citaService.cancelarCitaInteligente(citaId);
        return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/citastotales";
    }

    @GetMapping("/recepcionista/cita/marcar/{id}")
    public String marcarCitaPaciente(@PathVariable("id") int citaId) {
        citaService.marcarCita(citaId);
        return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/citastotales";
    }

    @GetMapping("/paciente/cita/modificar/{id}")
    public String mostrarFormularioModificar(@PathVariable("id") int citaId, Model model, HttpSession session) {
        Cita cita = citaService.obtenerPorId(citaId);

        // Asegúrate de tener el objeto horario también si necesitas más info
        Horario horarioActual = horarioService.obtenerPorId(cita.getHorario_id());
        int doctorId = horarioActual.getDoctor_id();
        int especialidad_id = doctorService.obetenerEspecialidadID(doctorId);
        // Obtener horarios disponibles del mismo doctor
        List<HorarioDTO> horariosDisponibles = horarioService.obtenerDisponiblesPorespecialidad(especialidad_id);

        model.addAttribute("cita", cita);
        model.addAttribute("horarios", horariosDisponibles);
        session.setAttribute("modificarCita_cita", cita);
        session.setAttribute("modificarCita_horarios", horariosDisponibles);

        return "redirect:/portalPaciente?contenido=paciente-dashboard/modificarCita";
    }

    @GetMapping("/recepcionista/cita/modificar/{id}")
    public String mostrarFormularioModificarCita(@PathVariable("id") int citaId, Model model, HttpSession session) {
        Cita cita = citaService.obtenerPorId(citaId);

        // Asegúrate de tener el objeto horario también si necesitas más info
        Horario horarioActual = horarioService.obtenerPorId(cita.getHorario_id());
        int doctorId = horarioActual.getDoctor_id();
        int especialidad_id = doctorService.obetenerEspecialidadID(doctorId);
        // Obtener horarios disponibles del mismo doctor
        List<HorarioDTO> horariosDisponibles = horarioService.obtenerDisponiblesPorespecialidad(especialidad_id);

        // Guardar en sesión
        session.setAttribute("citaModificar", cita);
        session.setAttribute("horariosDisponibles", horariosDisponibles);

        // Redirigir al portal con el contenido específico
        return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/modificarCita";
    }

    @PostMapping("/paciente/cita/modificar")
    public String modificarCita(@RequestParam int citaId, @RequestParam int nuevoHorarioId,
            RedirectAttributes redirectAttributes) {
        Cita cita = citaService.obtenerPorId(citaId);
        Horario horario = horarioService.obtenerPorId(nuevoHorarioId);
        if (citaService.verificarSiExisteCitaEnFechaHora(cita.getPaciente_id(), horario.getFecha(),
                horario.getHora())) {
            redirectAttributes.addFlashAttribute("notificacion", new Notificacion(
                    "Error en agendamiento",
                    "Usted ya tiene una cita programada para esta misma fecha y hora.",
                    "error"));
            return "redirect:/portalPaciente?contenido=paciente-dashboard/MisCitas";
        } else {
            citaService.cambiarDispnibilidadSI(citaId);
            citaService.actualizarHorarioCita(citaId, nuevoHorarioId);
            citaService.cambiarDispnibilidadNO(nuevoHorarioId);
            return "redirect:/portalPaciente?contenido=paciente-dashboard/MisCitas";
        }
    }

    @PostMapping("/recepcionista/cita/modificar")
    public String modificarCitaPaciente(@RequestParam int citaId, @RequestParam int nuevoHorarioId,
            RedirectAttributes redirectAttributes) {

        Cita cita = citaService.obtenerPorId(citaId);
        Horario horario = horarioService.obtenerPorId(nuevoHorarioId);
        if (citaService.verificarSiExisteCitaEnFechaHora(cita.getPaciente_id(), horario.getFecha(),
                horario.getHora())) {
            redirectAttributes.addFlashAttribute("notificacion", new Notificacion(
                    "Error en agendamiento",
                    "El paciente ya tiene una cita programada para esta misma fecha y hora.",
                    "error"));
            return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/citastotales";
        } else {
            if (cita.getEstado().equalsIgnoreCase("PROGRAMADA")) {
                citaService.cambiarDispnibilidadSI(citaId);
                citaService.actualizarHorarioCita(citaId, nuevoHorarioId);
                citaService.cambiarDispnibilidadNO(nuevoHorarioId);
                return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/citastotales";
            } else {
                citaService.cambiarDispnibilidadSI(citaId);
                citaService.actualizarHorarioCita(citaId, nuevoHorarioId);
                citaService.cambiarDispnibilidadNO(nuevoHorarioId);
                return "redirect:/portalRecepcionista?contenido=recepcionista-dashboard/historialTotal";
            }
        }
    }
}
