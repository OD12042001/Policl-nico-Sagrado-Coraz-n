package com.webapplication.PoliclinicoSagradoCorazon.model;

public class Doctor {
    private int id;
    private String nombre;
    private String cmp;
    private String imagen;
    private int especialidad_id; 
    
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
    public int getEspecialidad_id() {
        return especialidad_id;
    }
    public void setEspecialidad_id(int especialidad_id) {
        this.especialidad_id = especialidad_id;
    }
    
    
}
