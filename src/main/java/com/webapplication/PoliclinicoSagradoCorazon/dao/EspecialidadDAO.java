package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;

@Repository
public class EspecialidadDAO {

    @Autowired
    private JdbcTemplate jdbc;

    public Especialidad buscarPorId(int id) {
        try {
            String sql = "SELECT * FROM especialidad WHERE id = ?";
            return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Especialidad.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Especialidad> listarTodas() {
        String sql = "SELECT * FROM especialidad";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Especialidad.class));
    }

    public String obtenerNombrePorId(int id) {
        try {
            String sql = "SELECT nombre FROM especialidad WHERE id = ?";
            return jdbc.queryForObject(sql, String.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    public Especialidad obtenerPorId(int id) {
        try {
            String sql = "SELECT * FROM especialidad WHERE id = ?";
            return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Especialidad.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    public int obtenerIdPorNombre(String nombreEspecialidad) {
        String sql = "SELECT id FROM especialidad WHERE nombre = ?";
        try {
            return jdbc.queryForObject(sql, Integer.class, nombreEspecialidad);
        } catch (EmptyResultDataAccessException e) {
            return 0; // O lanza una excepción si prefieres
        }
    }

    public List<String> obtenerEspecialidadesUnicas() {
        String sql = """
                SELECT DISTINCT e.nombre
                FROM especialidad e
                JOIN doctor d ON e.id = d.especialidad_id
                ORDER BY e.nombre
                """;
        return jdbc.queryForList(sql, String.class);
    }

}
