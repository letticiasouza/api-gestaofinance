package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.EmailJaCadastradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.CreateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUsuarioService createUsuarioService;

    @Test
    void deveCriarUsuarioComSucesso() {

        CreateUsuarioRequest request =
                new CreateUsuarioRequest();

        request.setNome("Vitoria");
        request.setEmail("vitoria@email.com");
        request.setSenha("123456");

        when(usuarioRepositoryPort.buscarPorEmail(
                request.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("senhaHash");

        Usuario usuarioSalvo =
                new Usuario(
                        UUID.randomUUID(),
                        request.getNome(),
                        request.getEmail(),
                        "senhaHash"
                );

        when(usuarioRepositoryPort.salvar(any(Usuario.class)))
                .thenReturn(usuarioSalvo);

        UsuarioResponse response =
                createUsuarioService.criar(request);

        assertNotNull(response);

        assertEquals(
                request.getNome(),
                response.getNome());

        assertEquals(
                request.getEmail(),
                response.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {

        CreateUsuarioRequest request =
                new CreateUsuarioRequest();

        request.setNome("Vitoria");
        request.setEmail("vitoria@email.com");
        request.setSenha("123456");

        Usuario usuarioExistente =
                new Usuario(
                        UUID.randomUUID(),
                        "Vitoria",
                        "vitoria@email.com",
                        "123456"
                );

        when(usuarioRepositoryPort.buscarPorEmail(
                request.getEmail()))
                .thenReturn(Optional.of(usuarioExistente));

        assertThrows(
                EmailJaCadastradoException.class,
                () -> createUsuarioService.criar(request)
        );
    }
}