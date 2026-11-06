package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.ports.input.DeleteUsuarioUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
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

        usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(id));

        usuarioRepositoryPort.deletar(id);
    }
}