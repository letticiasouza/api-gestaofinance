package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.LoginInvalidoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.LoginRequest;
import com.ms1usuarios.dto.LoginResponse;
import com.ms1usuarios.ports.input.LoginUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService
        implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    private final PasswordEncoder passwordEncoder;

    public LoginService(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepositoryPort =
                usuarioRepositoryPort;

        this.passwordEncoder =
                passwordEncoder;
    }

    @Override
    public LoginResponse login(
            LoginRequest request) {

        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        Usuario usuario =
                usuarioRepositoryPort
                        .buscarPorEmail(email)
                        .orElseThrow(
                                LoginInvalidoException::new
                        );

        if (!passwordEncoder.matches(
                request.getSenha(),
                usuario.getSenha()
        )) {

            throw new LoginInvalidoException();
        }

        return new LoginResponse(
                "Login realizado com sucesso",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}