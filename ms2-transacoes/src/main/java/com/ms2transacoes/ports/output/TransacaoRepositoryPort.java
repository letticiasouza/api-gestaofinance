package com.ms2transacoes.ports.output;

import com.ms2transacoes.domain.model.Transacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransacaoRepositoryPort {

    Transacao salvar(Transacao transacao);

    Optional<Transacao> buscarPorId(UUID id);

    List<Transacao> listarPorUsuario(
            UUID usuarioId);

    List<Transacao> listarPorPeriodo(
            UUID usuarioId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

}