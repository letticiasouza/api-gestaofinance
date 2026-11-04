package com.ms1usuarios.ports.output;

import com.ms1usuarios.domain.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarTodos();

    void deletar(UUID id);
}