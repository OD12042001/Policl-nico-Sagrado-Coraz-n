package com.webapplication.PoliclinicoSagradoCorazon.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;

public interface HorarioConsultaRepository {
    List<Horario> listarPorDoctorId(int doctorId);
    List<Horario> obtenerHorariosDisponibles(int doctorId, LocalDate fechaActual, LocalTime horaActual);
    List<Horario> obtenerHorariosDisponiblesPorFecha(int doctorId, LocalDate fecha, LocalTime hora);
    Horario obtenerPorId(int id);
    List<Horario> obtenerDisponiblesPorDoctor(int doctorId);
    List<HorarioDTO> obtenerDisponiblesPorespecialidad(int especialidad_id);
    List<HorarioDTO> obtenerTodos();
    List<HorarioDTO> filtrarHorarios(String disponible, LocalDate fecha);
    boolean existeHorario(int doctorId, LocalDate fecha, LocalTime hora);
    boolean existeHorarioParaDoctor(int doctorId, LocalDate fecha, LocalTime hora, int ignorarId);
}
