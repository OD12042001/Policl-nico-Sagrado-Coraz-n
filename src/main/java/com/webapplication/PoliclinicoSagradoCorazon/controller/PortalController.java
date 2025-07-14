package com.webapplication.PoliclinicoSagradoCorazon.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webapplication.PoliclinicoSagradoCorazon.dto.AdministradorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.CitaDetalleDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.service.CitaService;
import com.webapplication.PoliclinicoSagradoCorazon.service.DoctorService;
import com.webapplication.PoliclinicoSagradoCorazon.service.HorarioService;
import com.webapplication.PoliclinicoSagradoCorazon.service.RecepcionistaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PortalController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RecepcionistaService recepcionistaService;

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/portalPaciente")
    public String mostrarPortalPaciente(@RequestParam(value = "contenido", required = false) String contenido,
            Model model, HttpSession session) {
        // para que se vea por defecto el perfil
        if (contenido == null || contenido.isBlank()) {
            contenido = "paciente-dashboard/perfil";
        }
        model.addAttribute("contenido", contenido);

        // Si va a cargar MisCitas, cargamos las citas programadas del paciente
        if (contenido.equals("paciente-dashboard/MisCitas")) {
            PacienteDTO paciente = (PacienteDTO) session.getAttribute("usuarioActual");
            if (paciente != null) {
                List<CitaDetalleDTO> citas = citaService.obtenerCitasProgramadasPorPaciente(paciente.getId());
                model.addAttribute("listaCitas", citas);
            }
        }

        // Si va a cargar historial, cargamos las citas atendidas del paciente
        if (contenido.equals("paciente-dashboard/historial")) {
            PacienteDTO paciente = (PacienteDTO) session.getAttribute("usuarioActual");
            if (paciente != null) {
                List<CitaDetalleDTO> citas = citaService.obtenerCitasAtendidasPorPaciente(paciente.getId());
                model.addAttribute("listaCitas", citas);
            }
        }
        return "portalPaciente";
    }

    @GetMapping("/portalRecepcionista")
    public String recepcionista(@RequestParam(value = "contenido", required = false) String contenido,
            @RequestParam(value = "especialidad", required = false) String especialidad,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model, HttpSession session) {
        // para que se vea por defecto el perfil
        if (contenido == null || contenido.isBlank()) {
            contenido = "recepcionista-dashboard/perfil";
        }
        model.addAttribute("contenido", contenido);

        // Si va a cargar citas totales, cargamos las citas programadas de todo los
        // pacientes
        if (contenido.equals("recepcionista-dashboard/citastotales")) {
            List<CitaDetalleDTO> citas;

            if ((especialidad != null && !especialidad.isBlank()) || fecha != null) {
                citas = citaService.buscarCitasProgramadasPorFiltros(especialidad, fecha);
            } else {
                citas = citaService.obtenerCitasProgramadasTotales();
            }

            // Para llenar el combo de especialidades
            List<String> listaEspecialidades = citaService.obtenerEspecialidadesProgramadas();

            model.addAttribute("listaCitas", citas);
            model.addAttribute("listaEspecialidades", listaEspecialidades);
            model.addAttribute("especialidadSeleccionada", especialidad);
            model.addAttribute("fechaSeleccionada", fecha);
        }

        // Si va a cargar historial, cargamos las citas atendidas del paciente
        if (contenido.equals("recepcionista-dashboard/historialTotal")) {
            List<CitaDetalleDTO> citas = citaService.obtenerCitasAtendidasTotales();
            model.addAttribute("listaCitas", citas);
        }

        return "portalRecepcionista";
    }

    @GetMapping("/portalAdministrador")
    public String admin(@RequestParam(value = "contenido", required = false) String contenido,
            @RequestParam(value = "disponible", required = false) String disponible,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model, HttpSession session) {

        // para que se vea por defecto el perfil
        if (contenido == null || contenido.isBlank()) {
            contenido = "administrador-dashboard/perfil";
        }
        model.addAttribute("contenido", contenido);

        if (contenido.equals("administrador-dashboard/doctores")) {
            List<DoctorDTO> listaDoctores = doctorService.obtenerTodosLosDoctores();
            model.addAttribute("listaDoctores", listaDoctores);
        }

        if (contenido.equals("administrador-dashboard/horarios")) {
            List<HorarioDTO> listaHorarios = horarioService.obtenerTodos(); // o con filtros si lo deseas
            List<DoctorDTO> listaDoctores = doctorService.obtenerTodosLosDoctores();
            System.out.println(disponible + " " + fecha);
            if ((disponible != null && !disponible.isBlank()) || fecha != null) {
                System.out.println("error1");
                listaHorarios = horarioService.filtrarHorarios(disponible, fecha);
            } else {
                listaHorarios = horarioService.obtenerTodos();
            }
            model.addAttribute("listaHorarios", listaHorarios);
            model.addAttribute("listaDoctores", listaDoctores); // para el formulario
        }

        if (contenido.equals("administrador-dashboard/Recepcionistas")) {
            List<RecepcionistaDTO> listaRecepcionistas = recepcionistaService.obtenerTodos();
            model.addAttribute("listaRecepcionistas", listaRecepcionistas);
        }

        return "portalAdministrador";
    }

    @GetMapping("/redireccionPortal")
    public String usuario(HttpSession session) {
        Object usuarioActual = session.getAttribute("usuarioActual");

        if (usuarioActual instanceof PacienteDTO paciente1) {
            return "redirect:/portalPaciente";
        } else if (usuarioActual instanceof RecepcionistaDTO recepcionista) {
            return "/portalRecepcionista";
        } else if (usuarioActual instanceof AdministradorDTO administrador) {
            return "/portalAdministrador";
        }
        return "error";
    }
}
