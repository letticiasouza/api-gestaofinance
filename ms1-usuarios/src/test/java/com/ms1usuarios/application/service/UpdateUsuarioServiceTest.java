package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.UpdateUsuarioRequest;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUsuarioService updateUsuarioService;

    @Test
    void deveAtualizarUsuarioComSucesso() {

        UUID id = UUID.randomUUID();

        Usuario usuario = new Usuario(
                id,
                "Nome Antigo",
                "antigo@email.com",
                "senhaAntigaHash"
        );

        UpdateUsuarioRequest request =
                new UpdateUsuarioRequest();

        request.setNome("Vitoria");
        request.setEmail("vitoria@email.com");
        request.setSenha("123456");

        when(usuarioRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.encode(anyString()))
                .thenReturn("senhaNovaHash");

        when(usuarioRepositoryPort.salvar(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponse response =
                updateUsuarioService.atualizar(id, request);

        assertNotNull(response);

        assertEquals(
                request.getNome(),
                response.getNome());

        assertEquals(
                request.getEmail(),
                response.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {

        UUID id = UUID.randomUUID();

        UpdateUsuarioRequest request =
                new UpdateUsuarioRequest();

        request.setNome("Vitoria");
        request.setEmail("vitoria@email.com");
        request.setSenha("123456");

        when(usuarioRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNaoEncontradoException.class,
                () -> updateUsuarioService.atualizar(id, request)
        );
    }
}