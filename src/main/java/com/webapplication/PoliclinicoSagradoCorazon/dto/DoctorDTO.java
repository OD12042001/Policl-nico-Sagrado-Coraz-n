package com.webapplication.PoliclinicoSagradoCorazon.dto;

public class DoctorDTO {
    private int id;
    private String nombre;
    private String especialidad;
    private String cmp;
    private int especialidadId;
    private String estado;
    
    public int getId() {
        return id;
    }
    public int getEspecialidadId() {
        return especialidadId;
    }
    public void setEspecialidadId(int especialidadId) {
        this.especialidadId = especialidadId;
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
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public String getCmp() {
        return cmp;
    }
    public void setCmp(String cmp) {
        this.cmp = cmp;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}
