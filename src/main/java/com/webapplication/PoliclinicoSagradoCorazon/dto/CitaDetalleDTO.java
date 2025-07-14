package com.webapplication.PoliclinicoSagradoCorazon.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDetalleDTO {
    private int citaId;
    private String doctorNombre;
    private String especialidad;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private String pacienteNombre;
    private String dniPaciente;

    public int getCitaId() {
        return citaId;
    }
    public void setCitaId(int citaId) {
        this.citaId = citaId;
    }
    public String getDoctorNombre() {
        return doctorNombre;
    }
    public void setDoctorNombre(String doctorNombre) {
        this.doctorNombre = doctorNombre;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    public String getPacienteNombre() {
        return pacienteNombre;
    }
    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }
    public String getDniPaciente() {
        return dniPaciente;
    }
    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

}
