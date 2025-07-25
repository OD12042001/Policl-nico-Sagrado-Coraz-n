package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;

public interface CitaComandoRepository {
    void insertarCita(Cita cita);
    void eliminarPorId(int citaId);
    void actualizarHorarioCita(int citaId, int nuevoHorarioId);
    void marcarCita(int citaID);
    void cancelarCita(int citaID);
}
