package com.webapplication.PoliclinicoSagradoCorazon.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dao.DoctorDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dao.EspecialidadDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dao.HorarioDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorHorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Doctor;
import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;

@Service
public class HorarioService {

    @Autowired
    private DoctorDAO doctorDAO;

    @Autowired
    private HorarioDAO horarioDAO;

    @Autowired
    private EspecialidadDAO especialidadDAO;

    public List<DoctorHorarioDTO> obtenerDoctoresConHorarios(LocalDate fechaFiltro) {
        // lsitar todos los doctores activos
        List<Doctor> doctores = doctorDAO.listarTodos();

        // declarar lista de doctores con sus horarios
        List<DoctorHorarioDTO> resultado = new ArrayList<>();

        // obetener variables de fecha y hora actual
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Doctor doc : doctores) {
            List<Horario> horarios;
            if (fechaFiltro != null) {
                // Si hay filtro por fecha, obtener solo los horarios de esa fecha
                horarios = horarioDAO.obtenerHorariosDisponiblesPorFecha(doc.getId(), fechaFiltro,
                        fechaFiltro.equals(fechaActual) ? horaActual : null);
            } else {
                // Sin filtro por fecha, obtener todos los horarios futuros
                horarios = horarioDAO.obtenerHorariosDisponibles(doc.getId(), fechaActual, horaActual);
            }
            // Filtrar horarios que estén disponibles
            List<Horario> horariosDisponibles = horarios.stream()
                    .filter(h -> "SI".equalsIgnoreCase(h.getDisponible()))
                    .collect(Collectors.toList());

            // Agrupar por fecha formateada solo los disponibles
            Map<String, List<Horario>> horariosPorFecha = horariosDisponibles.stream()
                    .collect(Collectors.groupingBy(h -> h.getFecha().format(formatter)));

            Especialidad esp = especialidadDAO.obtenerPorId(doc.getEspecialidad_id());

            DoctorHorarioDTO dto = new DoctorHorarioDTO();
            dto.setId(doc.getId());
            dto.setNombre(doc.getNombre());
            dto.setCmp(doc.getCmp());
            dto.setImagen(doc.getImagen());

            if (esp != null) {
                dto.setEspecialidad(esp.getNombre());
                dto.setPrecio(esp.getPrecio());
            }

            dto.setHorariosPorFecha(horariosPorFecha);
            resultado.add(dto);
        }

        return resultado;
    }

    public Horario obtenerPorId(int citaId) {
        return horarioDAO.obtenerPorId(citaId);
    }

    public List<Horario> obtenerDisponiblesPorDoctor(int doctorId) {
        return horarioDAO.obtenerDisponiblesPorDoctor(doctorId);
    }

    public List<HorarioDTO> obtenerDisponiblesPorespecialidad(int especialidad_id) {
        return horarioDAO.obtenerDisponiblesPorespecialidad(especialidad_id);
    }

    public List<HorarioDTO> obtenerTodos() {
        return horarioDAO.obtenerTodos();
    }

    public List<HorarioDTO> filtrarHorarios(String disponible, LocalDate fecha) {

        return horarioDAO.filtrarHorarios(disponible, fecha);
    }

    public boolean existeHorario(String nombreDoctor, LocalDate fecha, LocalTime hora) {
        int doctorId = doctorDAO.obtenerIDporNombre(nombreDoctor);
        return horarioDAO.existeHorario(doctorId, fecha, hora);
    }

    public void insertarHorario(HorarioDTO horario) {
        int doctorId = doctorDAO.obtenerIDporNombre(horario.getNombreDoctor());
        horario.setDoctorId(doctorId);
        horarioDAO.insertarHorario(horario);
    }

    public void actualizarDisponibilidad(int horarioId, String estado) {
        horarioDAO.actualizarDisponibilidad(horarioId, estado);
    }

    public boolean existeHorarioParaDoctor(int doctorId, LocalDate fecha, LocalTime hora, int horarioId) {
        return horarioDAO.existeHorarioParaDoctor(doctorId, fecha, hora, horarioId);
    }

    public void actualizar(Horario horario) {
        horarioDAO.actualizar(horario);
    }
}
