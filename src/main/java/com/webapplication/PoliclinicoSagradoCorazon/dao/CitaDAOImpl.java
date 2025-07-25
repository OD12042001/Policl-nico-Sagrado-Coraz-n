package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.CitaDetalleDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Cita;
import com.webapplication.PoliclinicoSagradoCorazon.repository.CitaComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.CitaConsultaRepository;

@Repository
public class CitaDAOImpl implements CitaConsultaRepository, CitaComandoRepository {

    private final JdbcTemplate jdbc;

    public CitaDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean verificarSiExisteCitaEnFechaHora(int idPaciente, LocalDate fecha, LocalTime hora) {
        String sql = """
                SELECT COUNT(*) > 0 AS existe
                FROM cita c
                JOIN horario h ON c.horario_id = h.id
                WHERE c.paciente_id = ?
                AND h.fecha = ?
                AND h.hora = ?
                AND c.estado = 'PROGRAMADA'
                """;

        return Boolean.TRUE.equals(jdbc.queryForObject(
                sql,
                Boolean.class,
                idPaciente,
                fecha,
                hora));
    }

    @Override
    public void insertarCita(Cita cita) {
        String sql = "INSERT INTO cita (paciente_id, horario_id, pago_id, estado) VALUES (?, ?, ?, ?)";
        jdbc.update(sql,
                cita.getPaciente_id(),
                cita.getHorario_id(),
                cita.getPago_id(),
                cita.getEstado());
    }

    @Override
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

    @Override
    public List<CitaDetalleDTO> obtenerCitasCanceladasPorPaciente(int pacienteId) {
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
                    WHERE c.paciente_id = ? AND c.estado = 'CANCELADA'
                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class), pacienteId);
    }

    @Override
    public List<CitaDetalleDTO> obtenerCitasProgramadasPorPacienteYFecha(int pacienteId, LocalDate fecha) {
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
                WHERE c.paciente_id = ?
                AND c.estado = 'PROGRAMADA'
                AND h.fecha = ?
                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class), pacienteId, fecha);
    }

    @Override
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

    @Override
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

    @Override
    public List<CitaDetalleDTO> obtenerCitasAtendidasPorPacienteYFecha(int pacienteId, LocalDate fecha) {
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
                    WHERE c.paciente_id = ? AND c.estado = 'ATENDIDA' AND h.fecha = ?
                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class), pacienteId, fecha);
    }

    @Override
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
                WHERE c.estado IN ('ATENDIDA', 'CANCELADA')
                """;
        return jdbc.query(sql, new BeanPropertyRowMapper<>(CitaDetalleDTO.class));
    }

    @Override
    public Cita obtenerPorId(int citaId) {
        String sql = "SELECT * FROM cita WHERE id = ?";
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Cita.class), citaId);
    }

    @Override
    public void eliminarPorId(int citaId) {
        jdbc.update("DELETE FROM cita WHERE id = ?", citaId);
    }

    @Override
    public void actualizarHorarioCita(int citaId, int nuevoHorarioId) {
        String sql = "UPDATE cita SET horario_id = ? , estado = 'PROGRAMADA' WHERE id = ?";
        jdbc.update(sql, nuevoHorarioId, citaId);
    }

    @Override
    public void marcarCita(int citaID) {
        String sql = "UPDATE cita c " +
                "JOIN horario h ON c.horario_id = h.id " +
                "SET c.estado = 'ATENDIDA', " +
                "h.disponible = 'NO', " +
                "h.estado = 'UTILIZADO' " +
                "WHERE c.id = ?";
        jdbc.update(sql, citaID);
    }

    @Override
    public void cancelarCita(int citaID) {
        String sql = "UPDATE cita c " +
                "JOIN horario h ON c.horario_id = h.id " +
                "SET c.estado = 'CANCELADA', " +
                "h.disponible = 'NO', " +
                "h.estado = 'NOUTILIZADO' " +
                "WHERE c.id = ?";
        jdbc.update(sql, citaID);
    }

    @Override
    public List<CitaDetalleDTO> buscarCitasProgramadasPorFiltros(String dni, String especialidad, LocalDate fecha) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.id AS citaId, p.nombre AS pacienteNombre, p.dni AS dniPaciente, " +
                        "d.nombre AS doctorNombre, e.nombre AS especialidad, h.fecha, h.hora, c.estado " +
                        "FROM cita c " +
                        "JOIN paciente p ON c.paciente_id = p.id " +
                        "JOIN horario h ON c.horario_id = h.id " +
                        "JOIN doctor d ON h.doctor_id = d.id " +
                        "JOIN especialidad e ON d.especialidad_id = e.id " +
                        "WHERE c.estado = 'PROGRAMADA' ");

        List<Object> params = new ArrayList<>();

        if (dni != null && !dni.isBlank()) {
            sql.append("AND p.dni = ? ");
            params.add(dni);
        }

        if (especialidad != null && !especialidad.isBlank()) {
            sql.append("AND e.nombre = ? ");
            params.add(especialidad);
        }

        if (fecha != null) {
            sql.append("AND h.fecha = ? ");
            params.add(fecha);
        }

        // Ordenar por fecha y hora ascendente
        sql.append("ORDER BY h.fecha ASC, h.hora ASC");

        return jdbc.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(CitaDetalleDTO.class));
    }

    @Override
    public List<CitaDetalleDTO> buscarCitasHistorialPorFiltros(String dni, String especialidad, LocalDate fecha) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.id AS citaId, p.nombre AS pacienteNombre, p.dni AS dniPaciente, " +
                        "d.nombre AS doctorNombre, e.nombre AS especialidad, h.fecha, h.hora, c.estado " +
                        "FROM cita c " +
                        "JOIN paciente p ON c.paciente_id = p.id " +
                        "JOIN horario h ON c.horario_id = h.id " +
                        "JOIN doctor d ON h.doctor_id = d.id " +
                        "JOIN especialidad e ON d.especialidad_id = e.id " +
                        "WHERE c.estado IN ('ATENDIDA', 'CANCELADA') ");

        List<Object> params = new ArrayList<>();

        if (dni != null && !dni.isBlank()) {
            sql.append("AND p.dni = ? ");
            params.add(dni);
        }

        if (especialidad != null && !especialidad.isBlank()) {
            sql.append("AND e.nombre = ? ");
            params.add(especialidad);
        }

        if (fecha != null) {
            sql.append("AND h.fecha = ? ");
            params.add(fecha);
        }

        sql.append("ORDER BY h.fecha DESC, h.hora DESC");

        return jdbc.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(CitaDetalleDTO.class));
    }

    @Override
    public List<String> obtenerEspecialidadesProgramadas() {
        String sql = "SELECT DISTINCT e.nombre " +
                "FROM cita c " +
                "JOIN horario h ON c.horario_id = h.id " +
                "JOIN doctor d ON h.doctor_id = d.id " +
                "JOIN especialidad e ON d.especialidad_id = e.id " +
                "WHERE c.estado = 'PROGRAMADA'";

        return jdbc.queryForList(sql, String.class);
    }

    @Override
    public List<String> obtenerEspecialidadesHistorial() {
        String sql = "SELECT DISTINCT e.nombre " +
                "FROM cita c " +
                "JOIN horario h ON c.horario_id = h.id " +
                "JOIN doctor d ON h.doctor_id = d.id " +
                "JOIN especialidad e ON d.especialidad_id = e.id " +
                "WHERE c.estado IN ('ATENDIDA', 'CANCELADA') " +
                "ORDER BY e.nombre";

        return jdbc.queryForList(sql, String.class);
    }

}