package com.webapplication.PoliclinicoSagradoCorazon.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapplication.PoliclinicoSagradoCorazon.dto.RecepcionistaDTO;
import com.webapplication.PoliclinicoSagradoCorazon.model.Usuario;
import com.webapplication.PoliclinicoSagradoCorazon.repository.RecepcionistaBusquedaRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.RecepcionistaComandoRepository;
import com.webapplication.PoliclinicoSagradoCorazon.repository.UsuarioRepository;

@Service
public class RecepcionistaService {

    private final RecepcionistaBusquedaRepository recepcionistaBusquedaRepository;
    private final RecepcionistaComandoRepository recepcionistaComandoRepository;
    private final UsuarioRepository UsuarioRepository;
    private final BCryptPasswordEncoder encoder;

    public RecepcionistaService(RecepcionistaBusquedaRepository recepcionistaBusquedaRepository,
            RecepcionistaComandoRepository recepcionistaComandoRepository,
            com.webapplication.PoliclinicoSagradoCorazon.repository.UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder encoder) {
        this.recepcionistaBusquedaRepository = recepcionistaBusquedaRepository;
        this.recepcionistaComandoRepository = recepcionistaComandoRepository;
        UsuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    public RecepcionistaDTO obtenerPorDni(String dni) {
        return recepcionistaBusquedaRepository.buscarPorDni(dni);
    }

    public List<RecepcionistaDTO> obtenerTodos() {
        return recepcionistaBusquedaRepository.obtenerTodos();
    }

    public RecepcionistaDTO obtenerPorId(int id) {
        return recepcionistaBusquedaRepository.obtenerPorId(id);
    }

    public void actualizar(RecepcionistaDTO recepcionista) {
        String passEncriptada = encoder.encode(recepcionista.getContraseña());
        recepcionista.setContraseña(passEncriptada);
        recepcionistaComandoRepository.actualizar(recepcionista);
    }

    public void insertar(RecepcionistaDTO recepcionista) {
        System.out.println("aqui estoyyyy 22222222222222222222222222222");
        String passEncriptada = encoder.encode(recepcionista.getContraseña());
        recepcionista.setContraseña(passEncriptada);
        Usuario usuario = new Usuario();
        usuario.setDni(recepcionista.getDni());
        usuario.setContraseña(recepcionista.getContraseña());
        usuario.setRol("RECEPCIONISTA");

        System.out.println(passEncriptada);
        System.out.println(recepcionista.getContraseña());
        System.out.println("aqui estoyyyy 333333333333333333333");
        UsuarioRepository.insertar(usuario);
        System.out.println("aqui estoyyyy 4444444444444444444444444");
        int usuario_id = UsuarioRepository.obtenerId(recepcionista.getDni());
        System.out.println(usuario_id);
        recepcionistaComandoRepository.insertar(recepcionista, usuario_id);
        System.out.println("ya inserto pe gaaaaaaaaaaaaaaaaaaa");
    }

    public void activar(String dni) {
        recepcionistaComandoRepository.activar(dni);
    }

    public void desactivar(String dni) {
        recepcionistaComandoRepository.desactivar(dni);
    }
}
