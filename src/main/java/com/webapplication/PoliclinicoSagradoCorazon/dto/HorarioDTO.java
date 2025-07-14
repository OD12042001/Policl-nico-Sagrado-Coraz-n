package com.webapplication.PoliclinicoSagradoCorazon.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class HorarioDTO {
    private int horarioID;
    private int doctorId;
    private String nombreDoctor;
    private LocalDate fecha;
    private LocalTime hora;
    private String disponible;
    private String nombreEspecialidad;
    
    public int getHorarioID() {
        return horarioID;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    public void setHorarioID(int horarioID) {
        this.horarioID = horarioID;
    }
    public String getNombreDoctor() {
        return nombreDoctor;
    }
    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    public String getDisponible() {
        return disponible;
    }
    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }
    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }
    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }

    

}
