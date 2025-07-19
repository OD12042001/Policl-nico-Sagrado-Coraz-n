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
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;
import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;
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
    public String mostrarPortalPaciente(
            @RequestParam(value = "contenido", required = false) String contenido,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFiltro,
            Model model, HttpSession session) {

        // Contenido por defecto
        if (contenido == null || contenido.isBlank()) {
            contenido = "paciente-dashboard/perfil";
        }
        model.addAttribute("contenido", contenido);

        PacienteDTO paciente = (PacienteDTO) session.getAttribute("usuarioActual");
        if (paciente != null) {
            if (contenido.equals("paciente-dashboard/MisCitas")) {
                List<CitaDetalleDTO> citas;
                if (fechaFiltro != null) {
                    citas = citaService.obtenerCitasProgramadasPorPacienteYFecha(paciente.getId(), fechaFiltro);
                } else {
                    citas = citaService.obtenerCitasProgramadasPorPaciente(paciente.getId());
                }
                model.addAttribute("listaCitas", citas);
                model.addAttribute("fechaFiltro", fechaFiltro); // Para mantener el filtro en la vista
            }

            if (contenido.equals("paciente-dashboard/historial")) {
                List<CitaDetalleDTO> citas;
                if (fechaFiltro != null) {
                    citas = citaService.obtenerCitasAtendidasPorPacienteYFecha(paciente.getId(), fechaFiltro);
                } else {
                    citas = citaService.obtenerCitasAtendidasPorPaciente(paciente.getId());
                }
                model.addAttribute("listaCitas", citas);
                model.addAttribute("fechaFiltro", fechaFiltro); // Para mantener el filtro en la vista
            }
            if (contenido != null && contenido.equals("paciente-dashboard/modificarCita")) {
                model.addAttribute("cita", session.getAttribute("modificarCita_cita"));
                model.addAttribute("horarios", session.getAttribute("modificarCita_horarios"));
                // Limpiar los atributos de sesión si ya no los necesitas
                session.removeAttribute("modificarCita_cita");
                session.removeAttribute("modificarCita_horarios");
            }
            if (contenido.equals("paciente-dashboard/canceladas")) {
                List<CitaDetalleDTO> citas;

                citas = citaService.obtenerCitasCanceladasPorPaciente(paciente.getId());

                model.addAttribute("listaCitasCanceladas", citas);
                model.addAttribute("fechaFiltro", fechaFiltro); // Para mantener el filtro en la vista
            }
        }
        return "portalPaciente";
    }

    @GetMapping("/portalRecepcionista")
    public String recepcionista(
            @RequestParam(value = "contenido", required = false) String contenido,
            @RequestParam(value = "dni", required = false) String dni,
            @RequestParam(value = "especialidad", required = false) String especialidad,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model, HttpSession session) {

        // Para que se vea por defecto el perfil
        if (contenido == null || contenido.isBlank()) {
            contenido = "recepcionista-dashboard/perfil";
        }
        model.addAttribute("contenido", contenido);

        // Si va a cargar citas totales
        if (contenido.equals("recepcionista-dashboard/citastotales")) {
            List<CitaDetalleDTO> citas;

            // Verificar si hay algún filtro activo
            if ((dni != null && !dni.isBlank()) ||
                    (especialidad != null && !especialidad.isBlank()) ||
                    fecha != null) {

                citas = citaService.buscarCitasProgramadasPorFiltros(dni, especialidad, fecha);
            } else {
                citas = citaService.obtenerCitasProgramadasTotales();
            }

            // Para llenar el combo de especialidades
            List<String> listaEspecialidades = citaService.obtenerEspecialidadesProgramadas();

            model.addAttribute("listaCitas", citas);
            model.addAttribute("listaEspecialidades", listaEspecialidades);
            model.addAttribute("dniFiltro", dni); // Nuevo atributo para el DNI
            model.addAttribute("especialidadSeleccionada", especialidad);
            model.addAttribute("fechaSeleccionada", fecha);
        }

        // Si va a cargar historial (atendidas/canceladas)
        if (contenido.equals("recepcionista-dashboard/historialTotal")) {
            List<CitaDetalleDTO> citas;

            if ((dni != null && !dni.isBlank()) ||
                    (especialidad != null && !especialidad.isBlank()) ||
                    fecha != null) {
                citas = citaService.buscarCitasHistorialPorFiltros(dni, especialidad, fecha);
            } else {
                citas = citaService.obtenerCitasAtendidasTotales();
            }

            List<String> listaEspecialidades = citaService.obtenerEspecialidadesHistorial();
            model.addAttribute("listaCitas", citas);
            model.addAttribute("listaEspecialidades", listaEspecialidades);
            model.addAttribute("dniFiltro", dni);
            model.addAttribute("especialidadSeleccionada", especialidad);
            model.addAttribute("fechaSeleccionada", fecha);
        }

        // Si va a modificar cita
        if (contenido.equals("recepcionista-dashboard/modificarCita")) {
            Cita cita = (Cita) session.getAttribute("citaModificar");
            List<HorarioDTO> horarios = (List<HorarioDTO>) session.getAttribute("horariosDisponibles");

            model.addAttribute("cita", cita);
            model.addAttribute("horarios", horarios);

            // Limpiar la sesión después de usar
            session.removeAttribute("citaModificar");
            session.removeAttribute("horariosDisponibles");
        }

        return "portalRecepcionista";
    }

    @GetMapping("/portalAdministrador")
    public String admin(@RequestParam(value = "contenido", required = false) String contenido,
            @RequestParam(value = "disponible", required = false) String disponible,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "especialidadFiltro", required = false) String especialidadFiltro,
            @RequestParam(value = "nombreFiltro", required = false) String nombreFiltro,
            Model model, HttpSession session) {

        // para que se vea por defecto el perfil
        if (contenido == null || contenido.isBlank()) {
            contenido = "administrador-dashboard/perfil";
        }
        model.addAttribute("contenido", contenido);

        if (contenido.equals("administrador-dashboard/doctores")) {
            List<DoctorDTO> listaDoctores;

            // Si hay filtros aplicados
            if ((especialidadFiltro != null && !especialidadFiltro.isBlank()) ||
                    (nombreFiltro != null && !nombreFiltro.isBlank())) {

                listaDoctores = doctorService.filtrarDoctores(nombreFiltro, especialidadFiltro);
            } else {
                listaDoctores = doctorService.obtenerTodosLosDoctores();
            }

            model.addAttribute("listaDoctores", listaDoctores);
            model.addAttribute("especialidades", doctorService.obtenerEspecialidadesUnicas());
            model.addAttribute("especialidadFiltro", especialidadFiltro);
            model.addAttribute("nombreFiltro", nombreFiltro);
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

        if (contenido.equals("administrador-dashboard/formularioDoctor")) {
            // Recuperar de la sesión
            List<Especialidad> especialidades = (List<Especialidad>) session.getAttribute("formDoctorEspecialidades");
            DoctorDTO doctor = (DoctorDTO) session.getAttribute("formDoctorData");

            if (especialidades != null && doctor != null) {
                model.addAttribute("listaEspecialidades", especialidades);
                model.addAttribute("doctor", doctor);

                // Limpiar la sesión después de usar
                session.removeAttribute("formDoctorEspecialidades");
                session.removeAttribute("formDoctorData");
            } else if (doctor == null) {
                model.addAttribute("listaEspecialidades", especialidades);
                model.addAttribute("doctor", new DoctorDTO());

                // Limpiar la sesión después de usar
                session.removeAttribute("formDoctorEspecialidades");
                session.removeAttribute("formDoctorData");
            } else {
                // Si no hay datos en sesión, redirigir a lista de doctores
                return "redirect:/portalAdministrador?contenido=administrador-dashboard/doctores";
            }
        }

        if (contenido.equals("administrador-dashboard/formularioHorario")) {
            // Recuperar de la sesión
            List<DoctorDTO> listaDoctores = (List<DoctorDTO>) session.getAttribute("listaDoctoresTEMP");
            HorarioDTO horarioDTO = (HorarioDTO) session.getAttribute("horarioTEMP");

            if (listaDoctores != null) {
                model.addAttribute("horario", new HorarioDTO());
                model.addAttribute("listaDoctores", listaDoctores);

                session.removeAttribute("listaDoctoresTEMP");
                session.removeAttribute("horarioTEMP");
            }else{
                return "redirect:/portalAdministrador?contenido=administrador-dashboard/horarios";
            }
        }

        if (contenido.equals("administrador-dashboard/formularioHorarioM")) {
            // Recuperar de la sesión
            List<DoctorDTO> listaDoctores = (List<DoctorDTO>) session.getAttribute("listaDoctoresTEMPM");
            Horario horario = (Horario) session.getAttribute("horarioTEMPM");

            if (listaDoctores != null && horario != null) {
                model.addAttribute("horario", horario);
                model.addAttribute("listaDoctores", listaDoctores);

                session.removeAttribute("listaDoctoresTEMPM");
                session.removeAttribute("horarioTEMPM");
            }else{
                return "redirect:/portalAdministrador?contenido=administrador-dashboard/horarios";
            }
        }

        if (contenido.equals("administrador-dashboard/formularioRecepcionista")) {
        RecepcionistaDTO recepcionistaDTO = (RecepcionistaDTO) session.getAttribute("recepcionista1");
        
        if (recepcionistaDTO != null) {
            model.addAttribute("recepcionista", recepcionistaDTO);
            session.removeAttribute("recepcionista1");
            
        } else {
            // Si no hay datos en sesión, redirigir a lista de recepcionistas
            return "redirect:/portalAdministrador?contenido=administrador-dashboard/Recepcionistas";
        }
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
