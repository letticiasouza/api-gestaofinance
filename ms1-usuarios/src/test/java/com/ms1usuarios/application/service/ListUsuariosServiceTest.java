package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUsuariosServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @InjectMocks
    private ListUsuariosService listUsuariosService;

    @Test
    void deveListarUsuariosComSucesso() {

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                "Vitoria",
                "vitoria@email.com",
                "123456"
        );

        when(usuarioRepositoryPort.listarTodos())
                .thenReturn(List.of(usuario));

        List<UsuarioResponse> response =
                listUsuariosService.listarTodos();

        assertEquals(1, response.size());

        assertEquals(
                usuario.getNome(),
                response.getFirst().getNome());

        assertEquals(
                usuario.getEmail(),
                response.getFirst().getEmail());
    }

    @Test
    void deveRetornarListaVazia() {

        when(usuarioRepositoryPort.listarTodos())
                .thenReturn(List.of());

        List<UsuarioResponse> response =
                listUsuariosService.listarTodos();

        assertTrue(response.isEmpty());
    }
}