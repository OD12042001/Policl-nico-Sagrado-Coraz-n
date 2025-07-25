package com.webapplication.PoliclinicoSagradoCorazon.repository;

import java.util.List;

import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Doctor;

public interface DoctorConsultaRepository {
    List<Doctor> listarTodos();
    Doctor buscarPorId(int id);
    int obetenerEspecialidadID(int doctorID);
    List<DoctorDTO> obtenerTodosLosDoctores();
    List<DoctorDTO> filtrarDoctores(String nombreFiltro, String especialidadFiltro);
    DoctorDTO obtenerPorId(int id);
    int obtenerIDporNombre(String nombreDoctor);
}
