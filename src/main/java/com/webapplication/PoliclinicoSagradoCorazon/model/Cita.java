package com.webapplication.PoliclinicoSagradoCorazon.model;

public class Cita {
    private int id;
    private int paciente_id;
    private int horario_id;
    private int pago_id;
    private String estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaciente_id() {
        return paciente_id;
    }

    public void setPaciente_id(int paciente_id) {
        this.paciente_id = paciente_id;
    }

    public int getHorario_id() {
        return horario_id;
    }

    public void setHorario_id(int horario_id) {
        this.horario_id = horario_id;
    }

    public int getPago_id() {
        return pago_id;
    }

    public void setPago_id(int pago_id) {
        this.pago_id = pago_id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
