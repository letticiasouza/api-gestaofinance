package com.ms1usuarios.application.service;

import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.ListUsuariosUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListUsuariosService implements ListUsuariosUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ListUsuariosService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public List<UsuarioResponse> listarTodos() {

        return usuarioRepositoryPort.listarTodos()
                .stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail()
                ))
                .toList();
    }
}