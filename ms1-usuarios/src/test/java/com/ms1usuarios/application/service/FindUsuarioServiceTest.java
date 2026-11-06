package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.UsuarioResponse;
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
class FindUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @InjectMocks
    private FindUsuarioService findUsuarioService;

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {

        UUID id = UUID.randomUUID();

        Usuario usuario = new Usuario(
                id,
                "Vitoria",
                "vitoria@email.com",
                "123456"
        );

        when(usuarioRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.of(usuario));

        UsuarioResponse response =
                findUsuarioService.buscarPorId(id);

        assertNotNull(response);

        assertEquals(id, response.getId());
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getEmail(), response.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {

        UUID id = UUID.randomUUID();

        when(usuarioRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNaoEncontradoException.class,
                () -> findUsuarioService.buscarPorId(id)
        );
    }
}