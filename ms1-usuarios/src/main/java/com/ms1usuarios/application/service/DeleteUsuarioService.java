package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.ports.input.DeleteUsuarioUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import com.ms1usuarios.domain.exception.AcessoNegadoException;
import com.ms1usuarios.domain.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteUsuarioService implements DeleteUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public DeleteUsuarioService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public void deletar(UUID id) {

        Usuario usuario =
                usuarioRepositoryPort
                        .buscarPorId(id)
                        .orElseThrow(() ->
                                new UsuarioNaoEncontradoException(id));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String emailAutenticado =
                authentication.getName();

        if (!usuario.getEmail()
                .equalsIgnoreCase(emailAutenticado)) {

            throw new AcessoNegadoException();
        }

        usuarioRepositoryPort.deletar(id);
    }
}