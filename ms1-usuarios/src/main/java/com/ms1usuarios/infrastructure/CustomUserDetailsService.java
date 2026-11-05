package com.ms1usuarios.infrastructure;

import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public CustomUserDetailsService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        Usuario usuario =
                usuarioRepositoryPort.buscarPorEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuário não encontrado"));

        return User.builder()
                .username(usuario.getEmail())
                .password("{noop}" + usuario.getSenha())
                .build();
    }
}