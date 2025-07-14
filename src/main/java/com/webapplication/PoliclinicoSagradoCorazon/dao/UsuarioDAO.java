package com.webapplication.PoliclinicoSagradoCorazon.dao;


import com.webapplication.PoliclinicoSagradoCorazon.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Usuario buscarPorDni(String dni) {
        try {
            String sql = "SELECT * FROM usuario WHERE dni = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Usuario.class), dni);
        } catch (Exception e) {
            return null;
        }
    }

    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuario (dni, contraseña, rol) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, usuario.getDni(), usuario.getContraseña(), usuario.getRol());
    }

    public void actualizarContraseña(String dni, String contraseña) {
        String sql = "UPDATE usuario SET contraseña = ? WHERE dni = ?";
        jdbcTemplate.update(sql, contraseña, dni);
    }

    public int obternerId(String dni){
        String sql = "SELECT id FROM usuario WHERE dni = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, dni);
    }

}
