package com.webapplication.PoliclinicoSagradoCorazon.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorHorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Doctor;
import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;
import com.webapplication.PoliclinicoSagradoCorazon.repository.DoctorConsultaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.EspecialidadConsultaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.HorarioComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.HorarioConsultaRepository;

@Service
public class HorarioService {

    private final DoctorConsultaRepository doctorConsultaRepository;
    private final HorarioConsultaRepository horarioConsultaRepository;
    private final HorarioComandoRepository horarioComandoRepository;
    private final EspecialidadConsultaRepository especialidadConsultaRepository;

    public HorarioService(DoctorConsultaRepository doctorConsultaRepository,
            HorarioConsultaRepository horarioConsultaRepository, HorarioComandoRepository horarioComandoRepository,
            EspecialidadConsultaRepository especialidadConsultaRepository) {
        this.doctorConsultaRepository = doctorConsultaRepository;
        this.horarioConsultaRepository = horarioConsultaRepository;
        this.horarioComandoRepository = horarioComandoRepository;
        this.especialidadConsultaRepository = especialidadConsultaRepository;
    }

    public List<DoctorHorarioDTO> obtenerDoctoresConHorarios(LocalDate fechaFiltro) {
        // lsitar todos los doctores activos
        List<Doctor> doctores = doctorConsultaRepository.listarTodos();

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
                horarios = horarioConsultaRepository.obtenerHorariosDisponiblesPorFecha(doc.getId(), fechaFiltro,
                        fechaFiltro.equals(fechaActual) ? horaActual : null);
            } else {
                // Sin filtro por fecha, obtener todos los horarios futuros
                horarios = horarioConsultaRepository.obtenerHorariosDisponibles(doc.getId(), fechaActual, horaActual);
            }
            // Filtrar horarios que estén disponibles
            List<Horario> horariosDisponibles = horarios.stream()
                    .filter(h -> "SI".equalsIgnoreCase(h.getDisponible()))
                    .collect(Collectors.toList());

            // Agrupar por fecha formateada solo los disponibles
            Map<String, List<Horario>> horariosPorFecha = horariosDisponibles.stream()
                    .collect(Collectors.groupingBy(h -> h.getFecha().format(formatter)));

            Especialidad esp = especialidadConsultaRepository.obtenerPorId(doc.getEspecialidad_id());

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
        return horarioConsultaRepository.obtenerPorId(citaId);
    }

    public List<Horario> obtenerDisponiblesPorDoctor(int doctorId) {
        return horarioConsultaRepository.obtenerDisponiblesPorDoctor(doctorId);
    }

    public List<HorarioDTO> obtenerDisponiblesPorespecialidad(int especialidad_id) {
        return horarioConsultaRepository.obtenerDisponiblesPorespecialidad(especialidad_id);
    }

    public List<HorarioDTO> obtenerTodos() {
        return horarioConsultaRepository.obtenerTodos();
    }

    public List<HorarioDTO> filtrarHorarios(String disponible, LocalDate fecha) {

        return horarioConsultaRepository.filtrarHorarios(disponible, fecha);
    }

    public boolean existeHorario(String nombreDoctor, LocalDate fecha, LocalTime hora) {
        int doctorId = doctorConsultaRepository.obtenerIDporNombre(nombreDoctor);
        return horarioConsultaRepository.existeHorario(doctorId, fecha, hora);
    }

    public void insertarHorario(HorarioDTO horario) {
        int doctorId = doctorConsultaRepository.obtenerIDporNombre(horario.getNombreDoctor());
        horario.setDoctorId(doctorId);
        horarioComandoRepository.insertarHorario(horario);
    }

    public void actualizarDisponibilidad(int horarioId, String estado) {
        horarioComandoRepository.actualizarDisponibilidad(horarioId, estado);
    }

    public boolean existeHorarioParaDoctor(int doctorId, LocalDate fecha, LocalTime hora, int horarioId) {
        return horarioConsultaRepository.existeHorarioParaDoctor(doctorId, fecha, hora, horarioId);
    }

    public void actualizar(Horario horario) {
        horarioComandoRepository.actualizar(horario);
    }

}
