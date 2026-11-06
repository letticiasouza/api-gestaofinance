package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.FindUsuarioUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindUsuarioService implements FindUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public FindUsuarioService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public UsuarioResponse buscarPorId(UUID id) {

        Usuario usuario =
                usuarioRepositoryPort.buscarPorId(id)
                        .orElseThrow(() ->
                                new UsuarioNaoEncontradoException(id));

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}