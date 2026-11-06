package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.UpdateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.UpdateUsuarioUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateUsuarioService implements UpdateUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public UpdateUsuarioService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public UsuarioResponse atualizar(
            UUID id,
            UpdateUsuarioRequest request) {

        Usuario usuario =
                usuarioRepositoryPort.buscarPorId(id)
                        .orElseThrow(() ->
                                new UsuarioNaoEncontradoException(id));

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());

        Usuario usuarioAtualizado =
                usuarioRepositoryPort.salvar(usuario);

        return new UsuarioResponse(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail()
        );
    }
}