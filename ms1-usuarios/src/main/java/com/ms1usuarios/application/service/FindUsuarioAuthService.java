package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.UsuarioAuthResponse;
import com.ms1usuarios.ports.input.FindUsuarioAuthUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FindUsuarioAuthService
        implements FindUsuarioAuthUseCase {

    private final UsuarioRepositoryPort
            usuarioRepositoryPort;

    public FindUsuarioAuthService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort =
                usuarioRepositoryPort;
    }

    @Override
    public UsuarioAuthResponse buscarPorEmail(
            String email) {

        Usuario usuario =
                usuarioRepositoryPort
                        .buscarPorEmail(
                                email.trim()
                                        .toLowerCase()
                        )
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuário não encontrado"
                                ));

        return new UsuarioAuthResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}