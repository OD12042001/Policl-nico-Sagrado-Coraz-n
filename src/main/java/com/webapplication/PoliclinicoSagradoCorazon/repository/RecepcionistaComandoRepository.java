package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;

public interface RecepcionistaComandoRepository {
    void insertar(RecepcionistaDTO recepcionista, int usuario_id);
    void actualizar(RecepcionistaDTO recepcionista);
    void activar(String dni);
    void desactivar(String dni);
}
