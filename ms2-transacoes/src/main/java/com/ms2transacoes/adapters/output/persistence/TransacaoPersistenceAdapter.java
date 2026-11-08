package com.ms2transacoes.adapters.output.persistence;

import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TransacaoPersistenceAdapter
        implements TransacaoRepositoryPort {

    private final SpringDataTransacaoRepository repository;

    public TransacaoPersistenceAdapter(
            SpringDataTransacaoRepository repository) {

        this.repository = repository;
    }

    @Override
    public Transacao salvar(
            Transacao transacao) {

        TransacaoEntity entity =
                new TransacaoEntity(
                        transacao.getId(),
                        transacao.getTipo(),
                        transacao.getStatus(),
                        transacao.getValor(),
                        transacao.getDataHora(),
                        transacao.getDescricao(),
                        transacao.getCategoria(),
                        transacao.getUsuarioOrigemId(),
                        transacao.getUsuarioDestinoId(),
                        transacao.getMotivoRejeicao()
                );

        TransacaoEntity saved =
                repository.save(entity);

        return new Transacao(
                saved.getId(),
                saved.getTipo(),
                saved.getStatus(),
                saved.getValor(),
                saved.getDataHora(),
                saved.getDescricao(),
                saved.getCategoria(),
                saved.getUsuarioOrigemId(),
                saved.getUsuarioDestinoId(),
                saved.getMotivoRejeicao()
        );
    }

    @Override
    public Optional<Transacao> buscarPorId(
            UUID id) {

        return repository.findById(id)
                .map(entity -> new Transacao(
                        entity.getId(),
                        entity.getTipo(),
                        entity.getStatus(),
                        entity.getValor(),
                        entity.getDataHora(),
                        entity.getDescricao(),
                        entity.getCategoria(),
                        entity.getUsuarioOrigemId(),
                        entity.getUsuarioDestinoId(),
                        entity.getMotivoRejeicao()
                ));
    }

    @Override
    public List<Transacao> listarPorUsuario(
            UUID usuarioId) {

        return repository.findByUsuarioOrigemId(usuarioId)
                .stream()
                .map(entity -> new Transacao(
                        entity.getId(),
                        entity.getTipo(),
                        entity.getStatus(),
                        entity.getValor(),
                        entity.getDataHora(),
                        entity.getDescricao(),
                        entity.getCategoria(),
                        entity.getUsuarioOrigemId(),
                        entity.getUsuarioDestinoId(),
                        entity.getMotivoRejeicao()
                ))
                .toList();
    }

    @Override
    public List<Transacao> listarPorPeriodo(
            UUID usuarioId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim) {

        return repository
                .findByUsuarioOrigemIdAndDataHoraBetween(
                        usuarioId,
                        dataInicio,
                        dataFim
                )
                .stream()
                .map(entity -> new Transacao(
                        entity.getId(),
                        entity.getTipo(),
                        entity.getStatus(),
                        entity.getValor(),
                        entity.getDataHora(),
                        entity.getDescricao(),
                        entity.getCategoria(),
                        entity.getUsuarioOrigemId(),
                        entity.getUsuarioDestinoId(),
                        entity.getMotivoRejeicao()
                ))
                .toList();
    }
}