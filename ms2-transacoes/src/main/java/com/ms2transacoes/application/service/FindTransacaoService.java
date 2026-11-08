package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.exception.TransacaoNaoEncontradaException;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.input.FindTransacaoUseCase;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindTransacaoService
        implements FindTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepositoryPort;

    public FindTransacaoService(
            TransacaoRepositoryPort transacaoRepositoryPort) {

        this.transacaoRepositoryPort =
                transacaoRepositoryPort;
    }

    @Override
    public TransacaoResponse buscarPorId(
            UUID id) {

        Transacao transacao =
                transacaoRepositoryPort
                        .buscarPorId(id)
                        .orElseThrow(() ->
                                new TransacaoNaoEncontradaException(id));

        return new TransacaoResponse(
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
    }
}