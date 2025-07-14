package com.webapplication.PoliclinicoSagradoCorazon.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class HorarioSeleccionadoDTO {
    private int horario_id;
    private String nombre;
    private String cmp;
    private String especialidad;
    private BigDecimal precio;
    private LocalDate fecha;
    private LocalTime hora;
    
    public int getHorario_id() {
        return horario_id;
    }
    public void setHorario_id(int horario_id) {
        this.horario_id = horario_id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCmp() {
        return cmp;
    }
    public void setCmp(String cmp) {
        this.cmp = cmp;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
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

    

}
