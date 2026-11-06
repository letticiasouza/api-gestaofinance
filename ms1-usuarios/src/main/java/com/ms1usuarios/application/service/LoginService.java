package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.LoginInvalidoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.LoginRequest;
import com.ms1usuarios.dto.LoginResponse;
import com.ms1usuarios.ports.input.LoginUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public LoginService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepositoryPort
                .buscarPorEmail(request.getEmail())
                .orElseThrow(LoginInvalidoException::new);

        if (!usuario.getSenha().equals(request.getSenha())) {
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