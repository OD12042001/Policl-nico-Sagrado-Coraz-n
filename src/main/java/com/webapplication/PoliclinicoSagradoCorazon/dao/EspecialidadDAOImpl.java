package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;
import com.webapplication.PoliclinicoSagradoCorazon.repository.EspecialidadConsultaRepository;

@Repository
public class EspecialidadDAOImpl implements EspecialidadConsultaRepository {

    private final JdbcTemplate jdbc;

    public EspecialidadDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Especialidad> listarTodasidNombre() {
        String sql = "SELECT id, nombre FROM especialidad ORDER BY id ASC";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Especialidad.class));
    }

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

    @Override
    public int obtenerIdPorNombre(String nombreEspecialidad) {
        try {
            String sql = "SELECT id FROM especialidad WHERE nombre = ?";
            Integer id = jdbc.queryForObject(sql, Integer.class, nombreEspecialidad);
            return id != null ? id : -1; // Retorna -1 si es null
        } catch (EmptyResultDataAccessException e) {
            return -1; // Valor consistente para "no encontrado"
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
