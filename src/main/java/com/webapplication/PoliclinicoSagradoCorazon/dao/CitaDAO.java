package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.CitaDetalleDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;

@Repository
public class CitaDAO {
    @Autowired
    private JdbcTemplate jdbc;

    public void insertarCita(Cita cita) {
        String sql = "INSERT INTO cita (paciente_id, horario_id, pago_id, estado) VALUES (?, ?, ?, ?)";
        jdbc.update(sql,
                cita.getPaciente_id(),
                cita.getHorario_id(),
                cita.getPago_id(),
                cita.getEstado());
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasPorPaciente(int pacienteId) {
        String sql = """
                    SELECT c.id AS citaId,
                        d.nombre AS doctorNombre,
                        e.nombre AS especialidad,
                        h.fecha,
                        h.hora,
                        c.estado
                    FROM cita c
                    JOIN horario h ON c.horario_id = h.id
                    JOIN doctor d ON h.doctor_id = d.id
                    JOIN especialidad e ON d.especialidad_id = e.id
                    WHERE c.paciente_id = ? AND c.estado = 'PROGRAMADA'
                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class), pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasProgramadasTotales() {
        String sql = """
                    SELECT c.id AS citaId,
                        d.nombre AS doctorNombre,
                        e.nombre AS especialidad,
                        h.fecha,
                        h.hora,
                        c.estado,
                        CONCAT(p.nombre, ' ', p.apellido_paterno, ' ', p.apellido_materno) AS pacienteNombre,
                        p.dni AS dniPaciente
                FROM cita c
                JOIN horario h ON c.horario_id = h.id
                JOIN paciente p ON c.paciente_id = p.id
                JOIN doctor d ON h.doctor_id = d.id
                JOIN especialidad e ON d.especialidad_id = e.id
                WHERE c.estado = 'PROGRAMADA'
                                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class));
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasPorPaciente(int pacienteId) {
        String sql = """
                    SELECT c.id AS citaId,
                        d.nombre AS doctorNombre,
                        e.nombre AS especialidad,
                        h.fecha,
                        h.hora,
                        c.estado
                    FROM cita c
                    JOIN horario h ON c.horario_id = h.id
                    JOIN doctor d ON h.doctor_id = d.id
                    JOIN especialidad e ON d.especialidad_id = e.id
                    WHERE c.paciente_id = ? AND c.estado = 'ATENDIDA'
                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class), pacienteId);
    }

    public List<CitaDetalleDTO> obtenerCitasAtendidasTotales() {
        String sql = """
                    SELECT c.id AS citaId,
                        d.nombre AS doctorNombre,
                        e.nombre AS especialidad,
                        h.fecha,
                        h.hora,
                        c.estado,
                        CONCAT(p.nombre, ' ', p.apellido_paterno, ' ', p.apellido_materno) AS pacienteNombre,
                        p.dni AS dniPaciente
                FROM cita c
                JOIN horario h ON c.horario_id = h.id
                JOIN paciente p ON c.paciente_id = p.id
                JOIN doctor d ON h.doctor_id = d.id
                JOIN especialidad e ON d.especialidad_id = e.id
                WHERE c.estado = 'ATENDIDA'
                """;
        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class));
    }

    public Cita obtenerPorId(int citaId) {
        String sql = "SELECT * FROM cita WHERE id = ?";
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Cita.class), citaId);
    }

    public void eliminarPorId(int citaId) {
        jdbc.update("DELETE FROM cita WHERE id = ?", citaId);
    }

    public void actualizarHorarioCita(int citaId, int nuevoHorarioId) {
        String sql = "UPDATE cita SET horario_id = ? WHERE id = ?";
        jdbc.update(sql, nuevoHorarioId, citaId);
    }

    public void marcarCita(int citaID) {
        String sql = "UPDATE cita SET estado = 'ATENDIDA' WHERE id = ?";
        jdbc.update(sql, citaID);
    }

    public List<CitaDetalleDTO> buscarCitasProgramadasPorFiltros(String especialidad, LocalDate fecha) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.id AS citaId, p.nombre AS pacienteNombre, p.dni AS dniPaciente, d.nombre AS doctorNombre, e.nombre AS especialidad, h.fecha, h.hora, c.estado "
                        +
                        "FROM cita c " +
                        "JOIN paciente p ON c.paciente_id = p.id " +
                        "JOIN horario h ON c.horario_id = h.id " +
                        "JOIN doctor d ON h.doctor_id = d.id " +
                        "JOIN especialidad e ON d.especialidad_id = e.id " +
                        "WHERE c.estado = 'PROGRAMADA' ");

        List<Object> params = new ArrayList<>();

        if (especialidad != null && !especialidad.isBlank()) {
            sql.append("AND e.nombre = ? ");
            params.add(especialidad);
        }

        if (fecha != null) {
            sql.append("AND h.fecha = ? ");
            params.add(fecha);
        }

        return jdbc.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(CitaDetalleDTO.class));
    }

    public List<String> obtenerEspecialidadesProgramadas() {
        String sql = "SELECT DISTINCT e.nombre " +
                "FROM cita c " +
                "JOIN horario h ON c.horario_id = h.id " +
                "JOIN doctor d ON h.doctor_id = d.id " +
                "JOIN especialidad e ON d.especialidad_id = e.id " +
                "WHERE c.estado = 'PROGRAMADA'";

        return jdbc.queryForList(sql, String.class);
    }

}
