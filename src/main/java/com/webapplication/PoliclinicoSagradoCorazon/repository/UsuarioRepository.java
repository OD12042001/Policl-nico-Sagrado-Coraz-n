package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.model.Usuario;

public interface UsuarioRepository {
    Usuario buscarPorDni(String dni);
    void insertar(Usuario usuario);
    void actualizarContraseña(String dni, String contraseña);
    int obtenerId(String dni);
}
