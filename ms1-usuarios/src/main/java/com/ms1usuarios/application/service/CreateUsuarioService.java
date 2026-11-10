package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.EmailJaCadastradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.CreateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.CreateUsuarioUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUsuarioService
        implements CreateUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    private final PasswordEncoder passwordEncoder;

    public CreateUsuarioService(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepositoryPort =
                usuarioRepositoryPort;

        this.passwordEncoder =
                passwordEncoder;
    }

    @Override
    public UsuarioResponse criar(
            CreateUsuarioRequest request) {

        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        usuarioRepositoryPort
                .buscarPorEmail(email)
                .ifPresent(usuario -> {

                    throw new EmailJaCadastradoException(
                            email
                    );
                });

        Usuario usuario =
                new Usuario(
                        UUID.randomUUID(),
                        request.getNome().trim(),
                        email,
                        passwordEncoder.encode(
                                request.getSenha()
                        )
                );

        Usuario usuarioSalvo =
                usuarioRepositoryPort
                        .salvar(usuario);

        return new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail()
        );
    }
}