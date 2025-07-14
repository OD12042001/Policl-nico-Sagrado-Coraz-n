package com.webapplication.PoliclinicoSagradoCorazon.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Horario {
    private int id;
    private int doctor_id;
    private LocalDate fecha;
    private LocalTime hora;
    private String disponible; // "SI" o "NO"
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getDoctor_id() {
        return doctor_id;
    }
    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
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

    
}
