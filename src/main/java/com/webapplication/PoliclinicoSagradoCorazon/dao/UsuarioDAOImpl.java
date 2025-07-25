package com.webapplication.PoliclinicoSagradoCorazon.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.model.Usuario;
import com.webapplication.PoliclinicoSagradoCorazon.repository.UsuarioRepository;

@Repository
public class UsuarioDAOImpl implements UsuarioRepository {

    private final JdbcTemplate jdbcTemplate; // Inyectamos la dependencia por constructor

    
    public UsuarioDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Usuario buscarPorDni(String dni) {
        try {
            String sql = "SELECT * FROM usuario WHERE dni = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Usuario.class), dni);
        } catch (EmptyResultDataAccessException e) {
            // No se encontró el usuario
            return null;
        } catch (DataAccessException e) {
            // Logear error (ej: con SLF4J)
            throw new RuntimeException("Error al buscar usuario por DNI: " + e.getMessage(), e);
        }
    }

    @Override
    public void insertar(Usuario usuario) {
        try {
            String sql = "INSERT INTO usuario (dni, contraseña, rol) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, usuario.getDni(), usuario.getContraseña(), usuario.getRol());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al insertar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarContraseña(String dni, String contraseña) {
        try {
            String sql = "UPDATE usuario SET contraseña = ? WHERE dni = ?";
            int updatedRows = jdbcTemplate.update(sql, contraseña, dni);
            if (updatedRows == 0) {
                throw new RuntimeException("No se encontró el usuario con DNI: " + dni);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al actualizar contraseña: " + e.getMessage(), e);
        }
    }

    @Override
    public int obtenerId(String dni) {
        try {
            String sql = "SELECT id FROM usuario WHERE dni = ?";
            Integer id = jdbcTemplate.queryForObject(sql, Integer.class, dni);
            return id != null ? id : -1; // Convierte null a -1
        } catch (EmptyResultDataAccessException e) {
            return -1;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener ID del usuario: " + e.getMessage(), e);
        }
    }
}
