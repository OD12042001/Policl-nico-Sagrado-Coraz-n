package com.webapplication.PoliclinicoSagradoCorazon.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.webapplication.PoliclinicoSagradoCorazon.dto.PacienteDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Paciente1;
import com.webapplication.PoliclinicoSagradoCorazon.model.Usuario;
import com.webapplication.PoliclinicoSagradoCorazon.repository.PacienteComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.PacienteConsultaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.UsuarioRepository;

@Service
public class PacienteService {

    private final PacienteComandoRepository pacienteComandoRepository;
    private final PacienteConsultaRepository pacienteConsultaRepository;
    private final UsuarioRepository UsuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PacienteService(PacienteComandoRepository pacienteComandoRepository,
            PacienteConsultaRepository pacienteConsultaRepository,
            com.webapplication.PoliclinicoSagradoCorazon.repository.UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.pacienteComandoRepository = pacienteComandoRepository;
        this.pacienteConsultaRepository = pacienteConsultaRepository;
        UsuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarPaciente(PacienteDTO dto) {
        System.out.println("✅ Insertando paciente en la base de datos...");

        // Crear usuario y guardar
        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setContraseña(passwordEncoder.encode(dto.getContraseña()));
        usuario.setRol("PACIENTE");
        UsuarioRepository.insertar(usuario);

        // Obtener el ID del usuario recién insertado
        Usuario usuarioGuardado = UsuarioRepository.buscarPorDni(dto.getDni());
        int usuarioId = usuarioGuardado.getId(); // <-- asegúrate de tener getId()

        // Insertar paciente con el usuario_id correcto
        pacienteComandoRepository.insertar(dto, usuarioId);
    }

    public PacienteDTO obtenerPorDni(String dni) {
        return pacienteConsultaRepository.buscarPorDni(dni);
    }

    public boolean existeDni(String dni) {
        return pacienteConsultaRepository.buscarPorDni(dni) != null;
    }

    public void actualizarDatosContacto(PacienteDTO paciente, PacienteDTO pacienteactual) {

        // Encripta la contraseña una sola vez
        String contraseñaCodificada = passwordEncoder.encode(paciente.getContraseña());

        // Actualiza en ambas tablas
        pacienteComandoRepository.actualizarDatosContacto(paciente);
        UsuarioRepository.actualizarContraseña(pacienteactual.getDni(), contraseñaCodificada);
    }

    public Paciente1 obtenerPorId(int idpaciente) {
        return pacienteConsultaRepository.obtenerPorId(idpaciente);
    }

}
