package com.webapplication.PoliclinicoSagradoCorazon.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dao.CitaDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dao.HorarioDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dao.PagoDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.CitaDetalleDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;
import com.webapplication.PoliclinicoSagradoCorazon.model.Pago;

@Service
public class CitaService {

    @Autowired
    private PagoDAO pagoDAO;

    @Autowired
    private CitaDAO citaDAO;

    @Autowired
    private HorarioDAO horarioDAO;

    public void registrarCita(BigDecimal precio, int pacienteId, int horarioId, String metodo) {
        // 1. Registrar el pago
        Pago pago = new Pago();
        pago.setMetodo(metodo); // Puedes mejorarlo si luego hay más métodos
        pago.setMonto(precio);
        pago.setEstado("PAGADO");

        int pagoId = pagoDAO.insertarPago(pago); // Este método debería guardar el pago y asignar el ID

        // 2. Registrar la cita
        Cita cita = new Cita();
        cita.setPaciente_id(pacienteId);
        cita.setHorario_id(horarioId);
        cita.setPago_id(pagoId);
        cita.setEstado("PROGRAMADA");

        citaDAO.insertarCita(cita);

        // 3. Marcar el horario como no disponible
        horarioDAO.marcarComoNoDisponible(horarioId);
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasPorPaciente(int pacienteId) {
        return citaDAO.obtenerCitasProgramadasPorPaciente(pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasCanceladasPorPaciente(int pacienteId) {
        return citaDAO.obtenerCitasCanceladasPorPaciente(pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasPorPacienteYFecha(int pacienteId, LocalDate fecha) {
        return citaDAO.obtenerCitasProgramadasPorPacienteYFecha(pacienteId, fecha);
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasTotales() {
        return citaDAO.obtenerCitasProgramadasTotales();
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasPorPaciente(int pacienteId) {
        return citaDAO.obtenerCitasAtendidasPorPaciente(pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasPorPacienteYFecha(int pacienteId,LocalDate fecha) {
        return citaDAO.obtenerCitasAtendidasPorPacienteYFecha(pacienteId,fecha);
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasTotales() {
        return citaDAO.obtenerCitasAtendidasTotales();
    }

    public void marcarCita(int citaID) {
        citaDAO.marcarCita(citaID);
    }

    public void cancelarCitaInteligente(int citaId) {
        citaDAO.cancelarCita(citaId);
    }

    public Cita obtenerPorId(int citaId) {
        return citaDAO.obtenerPorId(citaId);
    }

    public void actualizarHorarioCita(int citaId, int nuevoHorarioId) {
        citaDAO.actualizarHorarioCita(citaId, nuevoHorarioId);
    }

    public void cambiarDispnibilidadNO(int nuevoHorarioId) {
        horarioDAO.actualizarDisponibilidad(nuevoHorarioId, "NO");
    }

    public void cambiarDispnibilidadSI(int citaId) {
        horarioDAO.actualizarDisponibilidadPorCitaId(citaId, "SI");
    }

    public List<CitaDetalleDTO> buscarCitasProgramadasPorFiltros(String dni,String especialidad, LocalDate fecha) {
        return citaDAO.buscarCitasProgramadasPorFiltros(dni,especialidad, fecha);
    }

    public List<CitaDetalleDTO> buscarCitasHistorialPorFiltros(String dni,String especialidad, LocalDate fecha) {
        return citaDAO.buscarCitasHistorialPorFiltros(dni,especialidad, fecha);
    }

    public List<String> obtenerEspecialidadesProgramadas() {
        return citaDAO.obtenerEspecialidadesProgramadas();
    }

    public List<String> obtenerEspecialidadesHistorial() {
        return citaDAO.obtenerEspecialidadesHistorial();
    }

}
