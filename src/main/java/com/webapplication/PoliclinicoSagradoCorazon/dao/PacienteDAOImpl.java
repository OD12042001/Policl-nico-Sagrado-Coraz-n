package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Paciente1;
import com.webapplication.PoliclinicoSagradoCorazon.repository.PacienteComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.PacienteConsultaRepository;

@Repository
public class PacienteDAOImpl implements PacienteComandoRepository, PacienteConsultaRepository {
    private final JdbcTemplate jdbc;

    public PacienteDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insertar(PacienteDTO dto, int usuarioId) {
        String sql = "INSERT INTO paciente (usuario_id, nombre, apellido_paterno, apellido_materno, dni, fecha_nacimiento, sexo, correo, celular) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbc.update(sql,
                usuarioId,
                dto.getNombre(),
                dto.getApellidoPaterno(),
                dto.getApellidoMaterno(),
                dto.getDni(),
                dto.getFechaNacimiento(),
                dto.getSexo(),
                dto.getCorreo(),
                dto.getCelular());
    }

    @Override
    public PacienteDTO buscarPorDni(String dni) {
        String sql = "SELECT * FROM paciente WHERE dni = ?";
        List<PacienteDTO> lista = jdbc.query(sql, new Object[] { dni }, (rs, rowNum) -> {
            PacienteDTO dto = new PacienteDTO();
            dto.setId(rs.getInt("id"));
            dto.setDni(rs.getString("dni"));
            dto.setNombre(rs.getString("nombre"));
            dto.setApellidoPaterno(rs.getString("apellido_paterno"));
            dto.setApellidoMaterno(rs.getString("apellido_materno"));
            dto.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            dto.setSexo(rs.getString("sexo"));
            dto.setCorreo(rs.getString("correo"));
            dto.setCelular(rs.getString("celular"));
            return dto;
        });

        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public void actualizarDatosContacto(PacienteDTO paciente) {
        String sql = "UPDATE paciente SET correo = ?, celular = ? WHERE id = ?";
        jdbc.update(sql, paciente.getCorreo(), paciente.getCelular(), paciente.getId());
    }

    @Override
    public Paciente1 obtenerPorId(int idppaciente) {
        String sql = "SELECT * FROM paciente WHERE id = ?";
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Paciente1.class), idppaciente);
    }
}
