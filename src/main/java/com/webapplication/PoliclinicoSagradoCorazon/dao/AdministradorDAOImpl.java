package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.AdministradorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.repository.AdministradorConsultaRepository;

@Repository
public class AdministradorDAOImpl implements AdministradorConsultaRepository {

    private final JdbcTemplate jdbc;

    public AdministradorDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    // se puede llegar a reeemplazar con BeanPropertyRowMapper siempre y cuando los nombres de los atributos coincidan con los nombres de las columnas de la base de datos, de esa manera puede mapear
    @Override
    public AdministradorDTO buscarPorDni(String dni) {
        String sql = "SELECT * FROM administrador WHERE dni = ?";
        List<AdministradorDTO> lista = jdbc.query(sql, new Object[] { dni }, (rs, rowNum) -> {
            AdministradorDTO dto = new AdministradorDTO();
            dto.setId(rs.getInt("id"));
            dto.setDni(rs.getString("dni"));
            dto.setNombre(rs.getString("nombre"));
            dto.setApellido(rs.getString("apellido"));
            return dto;
        });

        return lista.isEmpty() ? null : lista.get(0);
    }
}
