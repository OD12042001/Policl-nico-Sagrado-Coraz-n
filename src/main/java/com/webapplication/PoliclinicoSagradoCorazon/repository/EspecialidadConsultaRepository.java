package com.webapplication.PoliclinicoSagradoCorazon.repository;

import java.util.List;

import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;

public interface EspecialidadConsultaRepository {
    List<Especialidad> listarTodasidNombre();
    Especialidad buscarPorId(int id);
    List<Especialidad> listarTodas();
    String obtenerNombrePorId(int id);
    Especialidad obtenerPorId(int id);
    int obtenerIdPorNombre(String nombreEspecialidad);
    List<String> obtenerEspecialidadesUnicas();
}
