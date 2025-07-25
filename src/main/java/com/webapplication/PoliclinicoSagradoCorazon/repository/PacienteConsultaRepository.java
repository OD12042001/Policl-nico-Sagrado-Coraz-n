package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Paciente1;

public interface PacienteConsultaRepository {
    PacienteDTO buscarPorDni(String dni);
    Paciente1 obtenerPorId(int idppaciente);
}
