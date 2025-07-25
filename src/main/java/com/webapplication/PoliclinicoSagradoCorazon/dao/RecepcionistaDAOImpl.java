package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.repository.RecepcionistaBusquedaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.RecepcionistaComandoRepository;

@Repository
public class RecepcionistaDAOImpl implements RecepcionistaBusquedaRepository, RecepcionistaComandoRepository {

    private final JdbcTemplate jdbc;

    public RecepcionistaDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public RecepcionistaDTO buscarPorDni(String dni) {
        String sql = "SELECT * FROM recepcionista WHERE dni = ? AND estado = 'ACTIVO'";
        List<RecepcionistaDTO> lista = jdbc.query(sql, new Object[] { dni }, (rs, rowNum) -> {
            RecepcionistaDTO dto = new RecepcionistaDTO();
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
    public List<RecepcionistaDTO> obtenerTodos() {
        String sql = """
                            SELECT p.id,
                p.dni,
                                    p.nombre,
                                    p.apellido_paterno AS apellidoPaterno,
                                    p.apellido_materno AS apellidoMaterno,
                                    p.fecha_nacimiento AS fechaNacimiento,
                                    p.sexo,
                                    p.correo,
                                    p.celular,
                                    u.contraseña,
                                    p.estado
                        FROM recepcionista p
                        JOIN usuario u ON p.usuario_id = u.id
                        """;
        return jdbc.query(sql, new BeanPropertyRowMapper<>(RecepcionistaDTO.class));
    }

    @Override
    public RecepcionistaDTO obtenerPorId(int id) {
        String sql = """
                            SELECT p.id,
                p.dni,
                                    p.nombre,
                                    p.apellido_paterno AS apellidoPaterno,
                                    p.apellido_materno AS apellidoMaterno,
                                    p.fecha_nacimiento AS fechaNacimiento,
                                    p.sexo,
                                    p.correo,
                                    p.celular,
                                    u.contraseña
                        FROM recepcionista p
                        JOIN usuario u ON p.usuario_id = u.id
                        WHERE p.id = ?
                        """;
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(RecepcionistaDTO.class), id);
    }

    @Override
    public void actualizar(RecepcionistaDTO recepcionista) {
        String sql = "UPDATE recepcionista SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                "correo = ?, celular = ? WHERE dni = ? ";

        jdbc.update(sql,
                recepcionista.getNombre(),
                recepcionista.getApellidoPaterno(),
                recepcionista.getApellidoMaterno(),
                recepcionista.getCorreo(),
                recepcionista.getCelular(),
                recepcionista.getDni());

        String sql1 = "UPDATE usuario SET contraseña = ? WHERE dni = ? ";

        jdbc.update(sql1,
                recepcionista.getContraseña(),
                recepcionista.getDni());
    }

    @Override
    public void insertar(RecepcionistaDTO recepcionista, int usuario_id) {

        String sql = "INSERT INTO recepcionista ( usuario_id, nombre, apellido_paterno, apellido_materno, dni, correo, celular) "
                +
                "VALUES (?,?, ?, ?, ?, ?, ?)";

        jdbc.update(sql,
                usuario_id,
                recepcionista.getNombre(),
                recepcionista.getApellidoPaterno(),
                recepcionista.getApellidoMaterno(),
                recepcionista.getDni(),
                recepcionista.getCorreo(),
                recepcionista.getCelular());
    }

    @Override
    public void activar(String dni) {

        String sql1 = "UPDATE recepcionista SET estado = 'ACTIVO' WHERE dni = ? ";
        jdbc.update(sql1, dni);

    }

    @Override
    public void desactivar(String dni) {

        String sql1 = "UPDATE recepcionista SET estado = 'INACTIVO' WHERE dni = ? ";
        jdbc.update(sql1, dni);

    }
}
