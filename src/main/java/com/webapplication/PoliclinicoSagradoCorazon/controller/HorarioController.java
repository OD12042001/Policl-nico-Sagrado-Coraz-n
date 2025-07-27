package com.webapplication.PoliclinicoSagradoCorazon.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorHorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioSeleccionadoDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.Notificacion;
import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;
import com.webapplication.PoliclinicoSagradoCorazon.repository.EspecialidadConsultaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.service.CitaService;
import com.webapplication.PoliclinicoSagradoCorazon.service.DoctorService;
import com.webapplication.PoliclinicoSagradoCorazon.service.HorarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EspecialidadConsultaRepository especialidadDAO;

    @Autowired
    private CitaService citaService;

    @GetMapping("/horario")
    public String mostrarHorarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String especialidad,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        List<DoctorHorarioDTO> lista = horarioService.obtenerDoctoresConHorarios(fecha);

        // Filtrado por nombre
        if (nombre != null && !nombre.isEmpty()) {
            lista = lista.stream()
                    .filter(d -> d.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .toList();
        }

        // Filtrado por especialidad
        if (especialidad != null && !especialidad.isEmpty()) {
            lista = lista.stream()
                    .filter(d -> d.getEspecialidad().equalsIgnoreCase(especialidad))
                    .toList();
        }

        model.addAttribute("doctores", lista);
        model.addAttribute("especialidades", especialidadDAO.listarTodas()); // <- asegúrate de tener este método
        return "horario";
    }

    @PostMapping("/guardarHorarioSeleccionado")
    public String guardarHorarioSeleccionado(
            @RequestParam int horario_id,
            @RequestParam String nombre,
            @RequestParam String cmp,
            @RequestParam String especialidad,
            @RequestParam BigDecimal precio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            HttpSession session) {

        // Si ya está autenticado, redirigir directamente al formulario
        HorarioSeleccionadoDTO dto = new HorarioSeleccionadoDTO();
        dto.setHorario_id(horario_id);
        dto.setNombre(nombre);
        dto.setCmp(cmp);
        dto.setEspecialidad(especialidad);
        dto.setPrecio(precio);
        dto.setFecha(fecha);
        dto.setHora(hora);

        session.setAttribute("horarioSeleccionado", dto);
        System.out.println(dto);
        System.out.println("guardarHorarioSeleccionado");
        return "redirect:/registro-cita";
    }

    @SuppressWarnings("null")
    @GetMapping("/registro-cita")
    public String mostrarFormularioCita(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        HorarioSeleccionadoDTO horario = (HorarioSeleccionadoDTO) session.getAttribute("horarioSeleccionado");
        System.out.println(horario);
        System.out.println("registro-cita");
        if (horario != null) {
            model.addAttribute("horarioSeleccionado", horario);
        }

        Object usuarioActual = session.getAttribute("usuarioActual");

        // Solo si es un PacienteDTO, agregamos al modelo
        if (usuarioActual instanceof PacienteDTO paciente1) {
            session.setAttribute("pacienteActual", paciente1);
            PacienteDTO paciente = (PacienteDTO) session.getAttribute("pacienteActual");
            if (paciente != null)
                model.addAttribute("pacienteActual", paciente);
            if (citaService.verificarSiExisteCitaEnFechaHora(paciente.getId(), horario.getFecha(), horario.getHora())) {
                redirectAttributes.addFlashAttribute("notificacion", new Notificacion(
                        "Error en agendamiento",
                        "Usted ya tiene una cita programada para esta misma fecha y hora.",
                        "error"));
                return "redirect:/horario";
            } else {
                return "formulario-cita";
            }

        } else if (usuarioActual instanceof RecepcionistaDTO recepcionista) {
            return "formulario-dni";
        } else if (usuarioActual == null) {
            return "redirect:/login";
        }
        return "error";
    }

    @GetMapping("/administrador/horario/nuevo")
    public String mostrarFormularioNuevoHorario(Model model, HttpSession session) {
        model.addAttribute("horario", new HorarioDTO());
        model.addAttribute("listaDoctores", doctorService.obtenerTodosLosDoctores());

        session.setAttribute("horarioTEMP", new HorarioDTO());
        session.setAttribute("listaDoctoresTEMP", doctorService.obtenerTodosLosDoctores());
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/formularioHorario";
    }

    @PostMapping("/administrador/horario/guardar")
    public String guardarHorario(@ModelAttribute HorarioDTO horario, RedirectAttributes redirectAttrs) {
        LocalDateTime fechaHoraIngresada = LocalDateTime.of(horario.getFecha(), horario.getHora());
        if (fechaHoraIngresada.isBefore(LocalDateTime.now())) {
            redirectAttrs.addFlashAttribute("error", "La fecha y hora deben ser futuras.");
            return "redirect:/administrador/horario/nuevo";
        }

        if (horarioService.existeHorario(horario.getNombreDoctor(), horario.getFecha(), horario.getHora())) {
            redirectAttrs.addFlashAttribute("error", "Este doctor ya tiene un horario en esa fecha y hora.");
            return "redirect:/administrador/horario/nuevo";
        }

        horarioService.insertarHorario(horario);
        redirectAttrs.addFlashAttribute("success", "Horario agregado exitosamente.");
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/horarios";
    }

    @GetMapping("/administrador/horario/apertura/{id}")
    public String habilitarHorario(@PathVariable("id") int horarioId, RedirectAttributes redirect) {
        horarioService.actualizarDisponibilidad(horarioId, "SI");
        redirect.addFlashAttribute("success", "Horario habilitado correctamente.");
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/horarios";
    }

    @GetMapping("/administrador/horario/cierre/{id}")
    public String deshabilitarHorario(@PathVariable("id") int horarioId, RedirectAttributes redirect) {
        horarioService.actualizarDisponibilidad(horarioId, "NO");
        redirect.addFlashAttribute("success", "Horario deshabilitado correctamente.");
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/horarios";
    }

    @GetMapping("/administrador/horario/modificar/{id}")
    public String mostrarFormularioModificarHorario(@PathVariable("id") int id, Model model, HttpSession session) {
        Horario horario = horarioService.obtenerPorId(id);
        List<DoctorDTO> listaDoctores = doctorService.obtenerTodosLosDoctores();

        model.addAttribute("horario", horario);
        model.addAttribute("listaDoctores", listaDoctores);

        session.setAttribute("horarioTEMPM", horario);
        session.setAttribute("listaDoctoresTEMPM", listaDoctores);
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/formularioHorarioM";
    }

    @PostMapping("/administrador/horario/actualizar")
    public String actualizarHorario(@ModelAttribute Horario horario, RedirectAttributes attr) {
        // Validación: No permitir fechas/horas pasadas
        LocalDateTime actual = LocalDateTime.now();
        LocalDateTime nuevoHorario = LocalDateTime.of(horario.getFecha(), horario.getHora());

        if (nuevoHorario.isBefore(actual)) {
            attr.addFlashAttribute("error", "La fecha y hora no pueden ser anteriores a la actual.");
            return "redirect:/administrador/horario/modificar/" + horario.getId();
        }

        // Validación: el doctor no debe tener ese mismo horario
        boolean yaExiste = horarioService.existeHorarioParaDoctor(horario.getDoctor_id(), horario.getFecha(),
                horario.getHora(), horario.getId());
        if (yaExiste) {
            attr.addFlashAttribute("error", "Ese doctor ya tiene un horario para esa fecha y hora.");
            return "redirect:/administrador/horario/modificar/" + horario.getId();
        }

        horarioService.actualizar(horario);
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/horarios";
    }

}
