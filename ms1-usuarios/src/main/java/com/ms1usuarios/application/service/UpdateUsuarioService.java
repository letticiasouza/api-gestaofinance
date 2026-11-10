package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.EmailJaCadastradoException;
import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.UpdateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.UpdateUsuarioUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateUsuarioService
        implements UpdateUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    private final PasswordEncoder passwordEncoder;

    public UpdateUsuarioService(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepositoryPort =
                usuarioRepositoryPort;

        this.passwordEncoder =
                passwordEncoder;
    }

    @Override
    public UsuarioResponse atualizar(
            UUID id,
            UpdateUsuarioRequest request) {

        Usuario usuario =
                usuarioRepositoryPort
                        .buscarPorId(id)
                        .orElseThrow(() ->
                                new UsuarioNaoEncontradoException(id));

        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        usuarioRepositoryPort
                .buscarPorEmail(email)
                .ifPresent(usuarioExistente -> {

                    if (!usuarioExistente
                            .getId()
                            .equals(id)) {

                        throw new EmailJaCadastradoException(
                                email
                        );
                    }
                });

        usuario.setNome(
                request.getNome().trim()
        );

        usuario.setEmail(email);

        usuario.setSenha(
                passwordEncoder.encode(
                        request.getSenha()
                )
        );

        Usuario usuarioAtualizado =
                usuarioRepositoryPort
                        .salvar(usuario);

        return new UsuarioResponse(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail()
        );
    }
}