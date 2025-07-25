package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;

public interface PacienteComandoRepository {
    void insertar(PacienteDTO dto, int usuarioId);
    void actualizarDatosContacto(PacienteDTO paciente);
}
