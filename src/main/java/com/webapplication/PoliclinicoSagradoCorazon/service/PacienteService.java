package com.webapplication.PoliclinicoSagradoCorazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dao.PacienteDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dao.UsuarioDAO;
import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Paciente1;
import com.webapplication.PoliclinicoSagradoCorazon.model.Usuario;

@Service
public class PacienteService {

    @Autowired
    private PacienteDAO pacienteDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registrarPaciente(PacienteDTO dto) {
        System.out.println("✅ Insertando paciente en la base de datos...");

        // Crear usuario y guardar
        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setContraseña(passwordEncoder.encode(dto.getContraseña()));
        usuario.setRol("PACIENTE");
        usuarioDAO.insertar(usuario);

        // Obtener el ID del usuario recién insertado
        Usuario usuarioGuardado = usuarioDAO.buscarPorDni(dto.getDni());
        int usuarioId = usuarioGuardado.getId(); // <-- asegúrate de tener getId()

        // Insertar paciente con el usuario_id correcto
        pacienteDAO.insertar(dto, usuarioId);
    }

    public PacienteDTO obtenerPorDni(String dni) {
        return pacienteDAO.buscarPorDni(dni);
    }

    public boolean existeDni(String dni) {
        return pacienteDAO.buscarPorDni(dni) != null;
    }

    public void actualizarDatosContacto(PacienteDTO paciente,PacienteDTO pacienteactual) {

        // Encripta la contraseña una sola vez
        String contraseñaCodificada = passwordEncoder.encode(paciente.getContraseña());

        // Actualiza en ambas tablas
        pacienteDAO.actualizarDatosContacto(paciente);
        usuarioDAO.actualizarContraseña(pacienteactual.getDni(), contraseñaCodificada);
    }

    public Paciente1 obtenerPorId(int idpaciente){
        return pacienteDAO.obtenerPorId(idpaciente);
    }

}
