package com.webapplication.PoliclinicoSagradoCorazon.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dto.CitaDetalleDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;
import com.webapplication.PoliclinicoSagradoCorazon.repository.CitaComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.CitaConsultaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.HorarioComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.PagoComandoRepository;

@Service
public class CitaService {

    private final PagoComandoRepository pagoComandoRepository;
    private final CitaConsultaRepository citaConsultaRepository;
    private final CitaComandoRepository citaComandoRepository;
    private final HorarioComandoRepository horarioComandoRepository;

    public CitaService(PagoComandoRepository pagoComandoRepository, CitaConsultaRepository citaConsultaRepository,
            CitaComandoRepository citaComandoRepository, HorarioComandoRepository horarioComandoRepository) {
        this.pagoComandoRepository = pagoComandoRepository;
        this.citaConsultaRepository = citaConsultaRepository;
        this.citaComandoRepository = citaComandoRepository;
        this.horarioComandoRepository = horarioComandoRepository;
    }

    public void registrarCita(BigDecimal precio, int pacienteId, int horarioId, String metodo) {
        // 1. Registrar el pago
        Pago pago = new Pago();
        pago.setMetodo(metodo); // Puedes mejorarlo si luego hay más métodos
        pago.setMonto(precio);
        pago.setEstado("PAGADO");

        int pagoId = pagoComandoRepository.insertarPago(pago); // Este método debería guardar el pago y asignar el ID

        // 2. Registrar la cita
        Cita cita = new Cita();
        cita.setPaciente_id(pacienteId);
        cita.setHorario_id(horarioId);
        cita.setPago_id(pagoId);
        cita.setEstado("PROGRAMADA");

        citaComandoRepository.insertarCita(cita);

        // 3. Marcar el horario como no disponible
        horarioDAO.marcarComoNoDisponible(horarioId);
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasPorPaciente(int pacienteId) {
        return citaConsultaRepository.obtenerCitasProgramadasPorPaciente(pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasCanceladasPorPaciente(int pacienteId) {
        return citaConsultaRepository.obtenerCitasCanceladasPorPaciente(pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasPorPacienteYFecha(int pacienteId, LocalDate fecha) {
        return citaConsultaRepository.obtenerCitasProgramadasPorPacienteYFecha(pacienteId, fecha);
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasTotales() {
        return citaConsultaRepository.obtenerCitasProgramadasTotales();
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasPorPaciente(int pacienteId) {
        return citaConsultaRepository.obtenerCitasAtendidasPorPaciente(pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasPorPacienteYFecha(int pacienteId, LocalDate fecha) {
        return citaConsultaRepository.obtenerCitasAtendidasPorPacienteYFecha(pacienteId, fecha);
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasTotales() {
        return citaConsultaRepository.obtenerCitasAtendidasTotales();
    }

    public void marcarCita(int citaID) {
        citaComandoRepository.marcarCita(citaID);
    }

    public void cancelarCitaInteligente(int citaId) {
        citaComandoRepository.cancelarCita(citaId);
    }

    public Cita obtenerPorId(int citaId) {
        return citaConsultaRepository.obtenerPorId(citaId);
    }

    public void actualizarHorarioCita(int citaId, int nuevoHorarioId) {
        citaComandoRepository.actualizarHorarioCita(citaId, nuevoHorarioId);
    }

    public void cambiarDispnibilidadNO(int nuevoHorarioId) {
        horarioComandoRepository.actualizarDisponibilidad(nuevoHorarioId, "NO");
    }

    public void cambiarDispnibilidadSI(int citaId) {
        horarioComandoRepository.actualizarDisponibilidadPorCitaId(citaId, "SI");
    }

    public List<CitaDetalleDTO> buscarCitasProgramadasPorFiltros(String dni, String especialidad, LocalDate fecha) {
        return citaConsultaRepository.buscarCitasProgramadasPorFiltros(dni, especialidad, fecha);
    }

    public List<CitaDetalleDTO> buscarCitasHistorialPorFiltros(String dni, String especialidad, LocalDate fecha) {
        return citaConsultaRepository.buscarCitasHistorialPorFiltros(dni, especialidad, fecha);
    }

    public List<String> obtenerEspecialidadesProgramadas() {
        return citaConsultaRepository.obtenerEspecialidadesProgramadas();
    }

    public List<String> obtenerEspecialidadesHistorial() {
        return citaConsultaRepository.obtenerEspecialidadesHistorial();
    }

    public boolean verificarSiExisteCitaEnFechaHora(int idPaciente, LocalDate fecha, LocalTime hora) {
        return citaConsultaRepository.verificarSiExisteCitaEnFechaHora(idPaciente, fecha, hora);
    }
}
