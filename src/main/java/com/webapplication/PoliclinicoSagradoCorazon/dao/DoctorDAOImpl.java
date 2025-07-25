package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Doctor;
import com.webapplication.PoliclinicoSagradoCorazon.repository.DoctorComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.DoctorConsultaRepository;

@Repository
public class DoctorDAOImpl implements DoctorConsultaRepository, DoctorComandoRepository {

    private final JdbcTemplate jdbc;

    public DoctorDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Doctor> listarTodos() {
        String sql = "SELECT DISTINCT d.* FROM doctor d " +
                "INNER JOIN horario h ON d.id = h.doctor_id " +
                "WHERE d.estado = 'ACTIVO' " +
                "AND h.disponible = 'SI' " +
                "AND h.estado = 'NOUTILIZADO'";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Doctor.class));
    }

    @Override
    public Doctor buscarPorId(int id) {
        try {
            String sql = "SELECT * FROM doctor WHERE id = ?";
            return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Doctor.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int obetenerEspecialidadID(int doctorID) {
        try {
            String sql = "SELECT especialidad_id FROM doctor WHERE id = ?";
            Integer id = jdbc.queryForObject(sql, Integer.class, doctorID);
            return id != null ? id : -1; // Retorna -1 si es null
        } catch (EmptyResultDataAccessException e) {
            return -1; // Valor consistente para "no encontrado"
        }
    }

    @Override
    public List<DoctorDTO> obtenerTodosLosDoctores() {
        String sql = """
                    SELECT d.id,
                        d.nombre,
                        e.nombre AS especialidad,
                        d.cmp,
                        d.estado
                FROM doctor d
                JOIN especialidad e ON d.especialidad_id = e.id
                """;
        return jdbc.query(sql, new BeanPropertyRowMapper<>(DoctorDTO.class));
    }

    @Override
    public List<DoctorDTO> filtrarDoctores(String nombreFiltro, String especialidadFiltro) {
        // Consulta SQL base
        StringBuilder sql = new StringBuilder("""
                SELECT d.id,
                        d.nombre,
                        e.nombre AS especialidad,
                        d.cmp,
                        d.estado
                FROM doctor d
                JOIN especialidad e ON d.especialidad_id = e.id
                """);

        // Lista para almacenar los parámetros
        List<Object> params = new ArrayList<>();

        // Condiciones para los filtros
        List<String> conditions = new ArrayList<>();

        if (nombreFiltro != null && !nombreFiltro.isBlank()) {
            conditions.add("LOWER(d.nombre) LIKE ?");
            params.add("%" + nombreFiltro.toLowerCase() + "%");
        }

        if (especialidadFiltro != null && !especialidadFiltro.isBlank()) {
            conditions.add("LOWER(e.nombre) = ?");
            params.add(especialidadFiltro.toLowerCase());
        }

        // Agregar WHERE si hay condiciones
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        // Ejecutar la consulta
        return jdbc.query(
                sql.toString(),
                params.toArray(),
                new BeanPropertyRowMapper<>(DoctorDTO.class));
    }

    @Override
    public DoctorDTO obtenerPorId(int id) {
        String sql = """
                    SELECT d.id,
                        d.nombre,
                        e.nombre AS especialidad,
                        d.cmp,
                        e.id AS especialidadId
                FROM doctor d
                JOIN especialidad e ON d.especialidad_id = e.id
                WHERE d.id = ?
                """;
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(DoctorDTO.class), id);
    }

    @Override
    public void insertar(DoctorDTO doctor, int especialidadID) {
        String sql = "INSERT INTO doctor (nombre, especialidad_id, cmp, imagen) VALUES (?, ?, ?, ?)";

        jdbc.update(
                sql,
                doctor.getNombre(),
                especialidadID,
                doctor.getCmp(),
                null // o "" si prefieres que no sea NULL
        );
    }

    @Override
    public void actualizar(DoctorDTO doctor, int especialidadID) {
        String sql = "UPDATE doctor SET nombre = ?, cmp = ?, especialidad_id = ? WHERE id = ?";

        jdbc.update(sql,
                doctor.getNombre(),
                doctor.getCmp(),
                especialidadID,
                doctor.getId());
    }

    @Override
    public int obtenerIDporNombre(String nombreDoctor) {
        try {
            String sql = "SELECT id FROM doctor WHERE nombre = ?";
            Integer id = jdbc.queryForObject(sql, Integer.class, nombreDoctor);
            return id != null ? id : -1; // Retorna -1 si es null
        } catch (EmptyResultDataAccessException e) {
            return -1; // Valor consistente para "no encontrado"
        }
    }

    @Override
    public void cambiarActivado(int iddoctor) {
        String sql = "UPDATE doctor SET estado = 'ACTIVO' WHERE id = ?";
        jdbc.update(sql, iddoctor);
    }

    @Override
    public void cambiarDesactivado(int iddoctor) {
        String sql = "UPDATE doctor SET estado = 'INACTIVO' WHERE id = ?";
        jdbc.update(sql, iddoctor);
    }

}
