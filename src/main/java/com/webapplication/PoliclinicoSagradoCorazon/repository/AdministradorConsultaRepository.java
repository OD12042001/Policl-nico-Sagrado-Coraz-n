package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.AdministradorDTO;

public interface AdministradorConsultaRepository {
    AdministradorDTO buscarPorDni(String dni);
}
