package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.HorarioDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Horario;

@Repository
public class HorarioDAO {

    @Autowired
    private JdbcTemplate jdbc;

    public List<Horario> listarPorDoctorId(int doctorId) {
        String sql = "SELECT * FROM horario WHERE doctor_id = ? ORDER BY fecha, hora";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Horario.class), doctorId);
    }

    // obtiene los horarios no utilizado y disponibles de la fecha y hora actual
    // para adelante
    public List<Horario> obtenerHorariosDisponibles(int doctorId, LocalDate fechaActual, LocalTime horaActual) {
        String sql = "SELECT * FROM horario WHERE doctor_id = ? " +
                "AND estado = 'NOUTILIZADO' " +
                "AND disponible = 'SI' " +
                "AND (fecha > ? OR (fecha = ? AND hora > ?)) " +
                "ORDER BY fecha, hora";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Horario.class), doctorId, fechaActual, fechaActual,
                horaActual);
    }

    //obtiene todos los horarios filtrados por fecha y de la hora actual para adelante
    public List<Horario> obtenerHorariosDisponiblesPorFecha(int doctorId, LocalDate fecha, LocalTime hora) {
        String sql;
        List<Object> params = new ArrayList<>();

        params.add(doctorId);
        params.add(fecha);

        if (hora != null) {
            // Si hay hora, significa que es la fecha actual y debemos filtrar horas futuras
            sql = "SELECT * FROM horario WHERE doctor_id = ? " +
                    "AND fecha = ? " +
                    "AND hora > ? " +
                    "AND estado = 'NOUTILIZADO' " +
                    "AND disponible = 'SI' " +
                    "ORDER BY hora";
            params.add(hora);
        } else {
            // Para otras fechas, mostramos todas las horas del día
            sql = "SELECT * FROM horario WHERE doctor_id = ? " +
                    "AND fecha = ? " +
                    "AND estado = 'NOUTILIZADO' " +
                    "AND disponible = 'SI' " +
                    "ORDER BY hora";
        }

        return jdbc.query(sql, new BeanPropertyRowMapper<>(Horario.class), params.toArray());
    }

    public void marcarComoNoDisponible(int horarioId) {
        String sql = "UPDATE horario SET disponible = 'NO', estado = 'NOUTILIZADO' WHERE id = ?";
        jdbc.update(sql, horarioId);
    }

    public Horario obtenerPorId(int id) {
        String sql = "SELECT * FROM horario WHERE id = ?";
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Horario.class), id);
    }

    public void actualizarDisponibilidad(int horarioId, String disponibilidad) {
        String sql = "UPDATE horario SET disponible = ?, estado = 'NOUTILIZADO'  WHERE id = ?";
        jdbc.update(sql, disponibilidad, horarioId);
    }

    public List<Horario> obtenerDisponiblesPorDoctor(int doctorId) {
        String sql = "SELECT * FROM horario WHERE doctor_id = ? AND disponible = 'SI'";

        return jdbc.query(sql, new Object[] { doctorId }, (rs, rowNum) -> {
            Horario h = new Horario();
            h.setId(rs.getInt("id"));
            h.setDoctor_id(rs.getInt("doctor_id"));
            h.setFecha(rs.getDate("fecha").toLocalDate());
            h.setHora(rs.getTime("hora").toLocalTime());
            h.setDisponible(rs.getString("disponible"));
            return h;
        });
    }

    public List<HorarioDTO> obtenerDisponiblesPorespecialidad(int especialidad_id) {
    String sql = """
                SELECT h.id AS horarioID,
                    d.nombre AS nombreDoctor,
                    h.fecha,
                    h.hora,
                    h.disponible,
                    e.nombre AS nombreEspecialidad
                FROM horario h
                JOIN doctor d ON h.doctor_id = d.id
                JOIN especialidad e ON d.especialidad_id = e.id
                WHERE e.id = ? 
                AND disponible = 'SI' 
                AND h.estado = 'NOUTILIZADO'
                AND (h.fecha > CURRENT_DATE OR 
                    (h.fecha = CURRENT_DATE AND h.hora > CURRENT_TIME))
                ORDER BY h.fecha ASC, h.hora ASC
            """;

    return jdbc.query(sql, new BeanPropertyRowMapper<>(HorarioDTO.class), especialidad_id);
}

    public void actualizarDisponibilidadPorCitaId(int citaId, String disponibilidad) {
        String sql = """
                    UPDATE horario
                    SET disponible = ?, estado = 'NOUTILIZADO' 
                    WHERE id = (
                        SELECT horario_id FROM cita WHERE id = ?
                    )
                """;

        jdbc.update(sql, disponibilidad, citaId);
    }

    public List<HorarioDTO> obtenerTodos() {
        String sql = """
                    SELECT h.id AS horarioID,
                        d.id AS doctorId,
                        d.nombre AS nombreDoctor,
                        h.fecha,
                        h.hora,
                        h.disponible,
                        e.nombre AS nombreEspecialidad
                    FROM horario h
                    JOIN doctor d ON h.doctor_id = d.id
                    JOIN especialidad e ON d.especialidad_id = e.id
                    WHERE h.estado = 'NOUTILIZADO'
                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(HorarioDTO.class));
    }

    public List<HorarioDTO> filtrarHorarios(String disponible, LocalDate fecha) {
        System.out.println("error 3");
        StringBuilder sql = new StringBuilder(
                "SELECT h.id AS horarioID, h.doctor_id AS doctorId, d.nombre AS nombreDoctor, h.fecha, h.hora, h.disponible "
                        +
                        "FROM horario h JOIN doctor d ON h.doctor_id = d.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (disponible != null && !disponible.isBlank()) {
            sql.append(" AND h.disponible = ? AND h.estado = 'NOUTILIZADO'");
            params.add(disponible);
        }

        if (fecha != null) {
            sql.append(" AND h.fecha = ? AND h.estado = 'NOUTILIZADO'");
            params.add(Date.valueOf(fecha));
        }

        return jdbc.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(HorarioDTO.class));
    }

    public boolean existeHorario(int doctorId, LocalDate fecha, LocalTime hora) {
        String sql = "SELECT COUNT(*) FROM horario WHERE doctor_id = ? AND fecha = ? AND hora = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, doctorId, fecha, hora);
        return count != null && count > 0;
    }

    public void insertarHorario(HorarioDTO horario) {
        String sql = "INSERT INTO horario (doctor_id, fecha, hora, disponible) VALUES (?, ?, ?, 'SI')";
        jdbc.update(sql, horario.getDoctorId(), horario.getFecha(), horario.getHora());
    }

    public boolean existeHorarioParaDoctor(int doctorId, LocalDate fecha, LocalTime hora, int ignorarId) {
        String sql = "SELECT COUNT(*) FROM horario WHERE doctor_id = ? AND fecha = ? AND hora = ? AND id <> ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, doctorId, fecha, hora, ignorarId);
        return count != null && count > 0;
    }

    public void actualizar(Horario horario) {
        String sql = "UPDATE horario SET doctor_id = ?, fecha = ?, hora = ? WHERE id = ?";
        jdbc.update(sql,
                horario.getDoctor_id(),
                horario.getFecha(),
                horario.getHora(),
                horario.getId());
    }

}
