package com.ms1usuarios.adapters.output.persistence;

import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioPersistenceAdapter
        implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository repository;

    public UsuarioPersistenceAdapter(
            SpringDataUsuarioRepository repository) {

        this.repository = repository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {

        UsuarioEntity entity =
                new UsuarioEntity(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenha()
                );

        UsuarioEntity saved =
                repository.save(entity);

        return new Usuario(
                saved.getId(),
                saved.getNome(),
                saved.getEmail(),
                saved.getSenha()
        );
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {

        return repository.findById(id)
                .map(entity ->
                        new Usuario(
                                entity.getId(),
                                entity.getNome(),
                                entity.getEmail(),
                                entity.getSenha()
                        ));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {

        return repository.findByEmail(email)
                .map(entity ->
                        new Usuario(
                                entity.getId(),
                                entity.getNome(),
                                entity.getEmail(),
                                entity.getSenha()
                        ));
    }

    @Override
    public List<Usuario> listarTodos() {

        return repository.findAll()
                .stream()
                .map(entity ->
                        new Usuario(
                                entity.getId(),
                                entity.getNome(),
                                entity.getEmail(),
                                entity.getSenha()
                        ))
                .toList();
    }

    @Override
    public void deletar(UUID id) {

        repository.deleteById(id);
    }
}