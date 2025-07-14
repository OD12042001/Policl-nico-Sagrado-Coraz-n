package com.webapplication.PoliclinicoSagradoCorazon.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;

public class DoctorHorarioDTO {
    private int id;
    private String nombre;
    private String cmp;
    private String imagen;
    private String especialidad;
    private BigDecimal precio;

    private Map<String, List<Horario>> horariosPorFecha; // Agrupados por fecha

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public Map<String, List<Horario>> getHorariosPorFecha() {
        return horariosPorFecha;
    }

    public void setHorariosPorFecha(Map<String, List<Horario>> horariosPorFecha) {
        this.horariosPorFecha = horariosPorFecha;
    }

}
