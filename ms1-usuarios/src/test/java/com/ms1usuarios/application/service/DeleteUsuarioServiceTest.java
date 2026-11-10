package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @InjectMocks
    private DeleteUsuarioService deleteUsuarioService;

    @Test
    void deveDeletarUsuarioComSucesso() {

        UUID id = UUID.randomUUID();

        Usuario usuario = new Usuario(
                id,
                "Vitoria",
                "vitoria@email.com",
                "123456"
        );

        when(usuarioRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.of(usuario));

        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "vitoria@email.com",
                                null
                        )
                );

        deleteUsuarioService.deletar(id);

        verify(usuarioRepositoryPort)
                .deletar(id);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {

        UUID id = UUID.randomUUID();

        when(usuarioRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNaoEncontradoException.class,
                () -> deleteUsuarioService.deletar(id)
        );

        verify(usuarioRepositoryPort, never())
                .deletar(any());
    }
}