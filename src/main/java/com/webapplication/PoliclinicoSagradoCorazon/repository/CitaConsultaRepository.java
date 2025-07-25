package com.webapplication.PoliclinicoSagradoCorazon.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.webapplication.PoliclinicoSagradoCorazon.dto.CitaDetalleDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;

public interface CitaConsultaRepository {
    boolean verificarSiExisteCitaEnFechaHora(int idPaciente, LocalDate fecha, LocalTime hora);
    List<CitaDetalleDTO> obtenerCitasProgramadasPorPaciente(int pacienteId);
    List<CitaDetalleDTO> obtenerCitasCanceladasPorPaciente(int pacienteId);
    List<CitaDetalleDTO> obtenerCitasProgramadasPorPacienteYFecha(int pacienteId, LocalDate fecha);
    List<CitaDetalleDTO> obtenerCitasProgramadasTotales();
    List<CitaDetalleDTO> obtenerCitasAtendidasPorPaciente(int pacienteId);
    List<CitaDetalleDTO> obtenerCitasAtendidasPorPacienteYFecha(int pacienteId, LocalDate fecha);
    List<CitaDetalleDTO> obtenerCitasAtendidasTotales();
    Cita obtenerPorId(int citaId);
    List<CitaDetalleDTO> buscarCitasProgramadasPorFiltros(String dni, String especialidad, LocalDate fecha);
    List<CitaDetalleDTO> buscarCitasHistorialPorFiltros(String dni, String especialidad, LocalDate fecha);
    List<String> obtenerEspecialidadesProgramadas();
    List<String> obtenerEspecialidadesHistorial();
}
