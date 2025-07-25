package com.webapplication.PoliclinicoSagradoCorazon.repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;

public interface DoctorComandoRepository {
    void insertar(DoctorDTO doctor, int especialidadID);
    void actualizar(DoctorDTO doctor, int especialidadID);
    void cambiarActivado(int iddoctor);
    void cambiarDesactivado(int iddoctor);
}
