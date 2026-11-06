package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.LoginInvalidoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.LoginRequest;
import com.ms1usuarios.dto.LoginResponse;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @InjectMocks
    private LoginService loginService;

    @Test
    void deveRealizarLoginComSucesso() {

        LoginRequest request = new LoginRequest();
        request.setEmail("vitoria@email.com");
        request.setSenha("123456");

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                "Vitoria",
                "vitoria@email.com",
                "123456"
        );

        when(usuarioRepositoryPort.buscarPorEmail(
                request.getEmail()))
                .thenReturn(Optional.of(usuario));

        LoginResponse response =
                loginService.login(request);

        assertNotNull(response);

        assertEquals(
                "Login realizado com sucesso",
                response.getMensagem());

        assertEquals(
                usuario.getNome(),
                response.getNome());

        assertEquals(
                usuario.getEmail(),
                response.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoExistir() {

        LoginRequest request = new LoginRequest();
        request.setEmail("vitoria@email.com");
        request.setSenha("123456");

        when(usuarioRepositoryPort.buscarPorEmail(
                request.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(
                LoginInvalidoException.class,
                () -> loginService.login(request)
        );
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForInvalida() {

        LoginRequest request = new LoginRequest();
        request.setEmail("vitoria@email.com");
        request.setSenha("senhaErrada");

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                "Vitoria",
                "vitoria@email.com",
                "123456"
        );

        when(usuarioRepositoryPort.buscarPorEmail(
                request.getEmail()))
                .thenReturn(Optional.of(usuario));

        assertThrows(
                LoginInvalidoException.class,
                () -> loginService.login(request)
        );
    }
}
