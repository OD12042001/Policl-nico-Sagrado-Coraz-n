package com.webapplication.PoliclinicoSagradoCorazon.service;

import com.webapplication.PoliclinicoSagradoCorazon.repository.UsuarioRepository;
import com.webapplication.PoliclinicoSagradoCorazon.model.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorDni(dni);
        if (usuario == null) {
            throw new UsernameNotFoundException("DNI no encontrado: " + dni);
        }

        return new User(
                usuario.getDni(),
                usuario.getContraseña(),
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol()))
        );
    }
}