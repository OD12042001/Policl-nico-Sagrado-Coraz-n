package com.webapplication.PoliclinicoSagradoCorazon.dto;

public class Notificacion {
    private String titulo;
    private String mensaje;
    private String tipo; // success, error, warning, info

    public Notificacion(String titulo, String mensaje, String tipo) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
