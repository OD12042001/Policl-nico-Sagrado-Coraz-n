package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;

public interface HorarioComandoRepository {
    void marcarComoNoDisponible(int horarioId);
    void actualizarDisponibilidad(int horarioId, String disponibilidad);
    void actualizarDisponibilidadPorCitaId(int citaId, String disponibilidad);
    void insertarHorario(HorarioDTO horario);
    void actualizar(Horario horario);
}
