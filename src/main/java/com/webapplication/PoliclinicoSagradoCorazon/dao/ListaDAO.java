package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.model.Especialidad;

@Repository
public class ListaDAO {
    
    @Autowired
    private JdbcTemplate jdbc;

    public List<Especialidad> listarTodas(){
        String sql = "SELECT id, nombre FROM especialidad ORDER BY id ASC";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Especialidad.class));
    }
}
