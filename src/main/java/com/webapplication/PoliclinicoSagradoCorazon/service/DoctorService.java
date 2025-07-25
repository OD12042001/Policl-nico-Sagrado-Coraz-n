package com.webapplication.PoliclinicoSagradoCorazon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.repository.DoctorComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.DoctorConsultaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.EspecialidadConsultaRepository;

@Service
public class DoctorService {

    private final DoctorConsultaRepository doctorConsultaRepository;
    private final DoctorComandoRepository doctorComandoRepository;
    private final EspecialidadConsultaRepository especialidadConsultaRepository;

    public DoctorService(DoctorConsultaRepository doctorConsultaRepository,
            DoctorComandoRepository doctorComandoRepository,
            EspecialidadConsultaRepository especialidadConsultaRepository) {
        this.doctorConsultaRepository = doctorConsultaRepository;
        this.doctorComandoRepository = doctorComandoRepository;
        this.especialidadConsultaRepository = especialidadConsultaRepository;
    }

    public int obetenerEspecialidadID(int doctorID) {
        return doctorConsultaRepository.obetenerEspecialidadID(doctorID);
    }

    public List<DoctorDTO> obtenerTodosLosDoctores() {
        return doctorConsultaRepository.obtenerTodosLosDoctores();
    }

    public List<DoctorDTO> filtrarDoctores(String nombreFiltro, String especialidadFiltro) {
        return doctorConsultaRepository.filtrarDoctores(nombreFiltro, especialidadFiltro);
    }

    public DoctorDTO obtenerPorId(int id) {
        return doctorConsultaRepository.obtenerPorId(id);
    }

    public void insertar(DoctorDTO doctor) {
        int especialidadID = especialidadConsultaRepository.obtenerIdPorNombre(doctor.getEspecialidad());
        doctorComandoRepository.insertar(doctor, especialidadID);

    }

    public void actualizar(DoctorDTO doctor) {
        int especialidadID = especialidadConsultaRepository.obtenerIdPorNombre(doctor.getEspecialidad());
        doctorComandoRepository.actualizar(doctor, especialidadID);

    }

    public List<String> obtenerEspecialidadesUnicas() {
        return especialidadConsultaRepository.obtenerEspecialidadesUnicas();
    }

    public void cambiarActivado(int iddoctor) {
        doctorComandoRepository.cambiarActivado(iddoctor);
    }

    public void cambiarDesactivado(int iddoctor) {
        doctorComandoRepository.cambiarDesactivado(iddoctor);
    }
}
