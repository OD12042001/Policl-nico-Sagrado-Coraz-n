package com.webapplication.PoliclinicoSagradoCorazon.repository;

import java.util.List;

import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;

public interface RecepcionistaBusquedaRepository {
    RecepcionistaDTO buscarPorDni(String dni);
    List<RecepcionistaDTO> obtenerTodos();
    RecepcionistaDTO obtenerPorId(int id);
}
