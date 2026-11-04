package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.EmailJaCadastradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.CreateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.CreateUsuarioUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUsuarioService implements CreateUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public CreateUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public UsuarioResponse criar(CreateUsuarioRequest request) {

        usuarioRepositoryPort.buscarPorEmail(request.getEmail())
                .ifPresent(usuario -> {
                    throw new EmailJaCadastradoException(request.getEmail());
                });

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                request.getNome(),
                request.getEmail(),
                request.getSenha()
        );

        Usuario usuarioSalvo =
                usuarioRepositoryPort.salvar(usuario);

        return new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail()
        );
    }
}