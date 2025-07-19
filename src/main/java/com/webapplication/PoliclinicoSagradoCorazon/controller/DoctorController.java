package com.webapplication.PoliclinicoSagradoCorazon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;
import com.webapplication.PoliclinicoSagradoCorazon.service.DoctorService;
import com.webapplication.PoliclinicoSagradoCorazon.service.EspecialidadService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping("/administrador/doctor/nuevo")
    public String mostrarFormularioNuevoDoctor(Model model,HttpSession session) {

        List<Especialidad> listaEspecialidades = especialidadService.listarTodas();
        model.addAttribute("listaEspecialidades", listaEspecialidades);

        model.addAttribute("doctor", new DoctorDTO());
        session.setAttribute("formDoctorEspecialidades", listaEspecialidades);
        session.setAttribute("formDoctorData", new DoctorDTO());

        return "redirect:/portalAdministrador?contenido=administrador-dashboard/formularioDoctor";
    }

    @GetMapping("/administrador/doctor/modificar")
    public String mostrarFormularioModificar(@RequestParam("doctorSeleccionado") int id, Model model,
            HttpSession session) {

        List<Especialidad> listaEspecialidades = especialidadService.listarTodas();
        model.addAttribute("listaEspecialidades", listaEspecialidades);

        DoctorDTO doctor = doctorService.obtenerPorId(id);
        model.addAttribute("doctor", doctor);

        session.setAttribute("formDoctorEspecialidades", listaEspecialidades);
        session.setAttribute("formDoctorData", doctor);
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/formularioDoctor";
    }

    @GetMapping("/administrador/doctor/activar")
    public String mostrarActivado(@RequestParam("doctorSeleccionado") int id, Model model) {

        doctorService.cambiarActivado(id);
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/doctores";
    }

    @GetMapping("/administrador/doctor/desactivar")
    public String mostrarDesactivado(@RequestParam("doctorSeleccionado") int id, Model model) {

        doctorService.cambiarDesactivado(id);
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/doctores";
    }

    @PostMapping("/administrador/doctor/guardar")
    public String guardarDoctor(@ModelAttribute("doctor") DoctorDTO doctor) {
        if (doctor.getId() == 0) {
            doctorService.insertar(doctor);
        } else {
            doctorService.actualizar(doctor);
        }
        return "redirect:/portalAdministrador?contenido=administrador-dashboard/doctores";
    }

    /*
     * METODO ES REVISION, RECORDAR QUE SI SE ELIMINAR SE ELIMINARIA TODO LOS
     * HROARIOS Y CITAS QUE TIENEN VINCULADO, MEJOR OPCION PONER ESTADO INACTIVO
     * 
     * @GetMapping("/administrador/doctor/eliminar")
     * public String eliminarDoctor(@RequestParam("doctorSeleccionado") int id) {
     * doctorService.eliminar(id);
     * return
     * "redirect:/portalAdministrador?contenido=administrador-dashboard/doctores";
     * }
     */
}
