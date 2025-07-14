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

    public List<Horario> obtenerPorDoctorYFechas(int doctorId, LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = "SELECT * FROM horario WHERE doctor_id = ? AND fecha BETWEEN ? AND ? ORDER BY fecha, hora";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Horario.class), doctorId, fechaInicio, fechaFin);
    }

    public void marcarComoNoDisponible(int horarioId) {
        String sql = "UPDATE horario SET disponible = 'NO' WHERE id = ?";
        jdbc.update(sql, horarioId);
    }

    public Horario obtenerPorId(int id) {
        String sql = "SELECT * FROM horario WHERE id = ?";
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Horario.class), id);
    }

    public void actualizarDisponibilidad(int horarioId, String disponibilidad) {
        String sql = "UPDATE horario SET disponible = ? WHERE id = ?";
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
                    WHERE e.id = ? AND disponible = 'SI'
                """;

        return jdbc.query(sql, new BeanPropertyRowMapper<>(HorarioDTO.class), especialidad_id);
    }

    public void actualizarDisponibilidadPorCitaId(int citaId, String disponibilidad) {
        String sql = """
                    UPDATE horario
                    SET disponible = ?
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
            sql.append(" AND h.disponible = ?");
            params.add(disponible);
        }

        if (fecha != null) {
            sql.append(" AND h.fecha = ?");
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
