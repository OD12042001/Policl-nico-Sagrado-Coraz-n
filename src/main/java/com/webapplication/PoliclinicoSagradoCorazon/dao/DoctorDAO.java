package com.webapplication.PoliclinicoSagradoCorazon.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webapplication.PoliclinicoSagradoCorazon.dto.DoctorDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Doctor;

@Repository
public class DoctorDAO {

    @Autowired
    private JdbcTemplate jdbc;

    public List<Doctor> listarTodos() {
        String sql = "SELECT * FROM doctor";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Doctor.class));
    }

    public Doctor buscarPorId(int id) {
        try {
            String sql = "SELECT * FROM doctor WHERE id = ?";
            return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Doctor.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    public int obetenerEspecialidadID(int doctorID) {
        String sql = "SELECT especialidad_id FROM doctor WHERE id = ?";
        return jdbc.queryForObject(sql, int.class, doctorID);
    }

    public List<DoctorDTO> obtenerTodosLosDoctores() {
        String sql = """
                    SELECT d.id,
                        d.nombre,
                        e.nombre AS especialidad,
                        d.cmp
                FROM doctor d
                JOIN especialidad e ON d.especialidad_id = e.id
                """;
        return jdbc.query(sql, new BeanPropertyRowMapper<>(DoctorDTO.class));
    }

    public DoctorDTO obtenerPorId(int id) {
        String sql = """
                    SELECT d.id,
                        d.nombre,
                        e.nombre AS especialidad,
                        d.cmp,
                        e.id AS especialidadId
                FROM doctor d
                JOIN especialidad e ON d.especialidad_id = e.id
                WHERE d.id = ?
                """;
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(DoctorDTO.class), id);
    }

    public void insertar(DoctorDTO doctor, int especialidadID) {
        String sql = "INSERT INTO doctor (nombre, especialidad_id, cmp, imagen) VALUES (?, ?, ?, ?)";

        jdbc.update(
                sql,
                doctor.getNombre(),
                especialidadID,
                doctor.getCmp(),
                null // o "" si prefieres que no sea NULL
        );
    }

    public void actualizar(DoctorDTO doctor, int especialidadID) {
        String sql = "UPDATE doctor SET nombre = ?, cmp = ?, especialidad_id = ? WHERE id = ?";

        jdbc.update(sql,
                doctor.getNombre(),
                doctor.getCmp(),
                especialidadID,
                doctor.getId());
    }

    public int obtenerIDporNombre(String nombreDoctor){
            String sql = "SELECT id FROM doctor WHERE nombre = ?";
            return jdbc.queryForObject(sql, Integer.class, nombreDoctor);
    }

    /*
     * METODO ES REVISION, RECORDAR QUE SI SE ELIMINAR SE ELIMINARIA TODO LOS
     * HROARIOS Y CITAS QUE TIENEN VINCULADO, MEJOR OPCION PONER ESTADO INACTIVO
     * public void eliminar(int id){
     * String sql = "DELETE FROM doctor WHERE id = ?";
     * jdbc.update(sql, id);
     * }
     */
}
