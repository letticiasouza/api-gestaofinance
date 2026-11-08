package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.input.ListTransacoesUseCase;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ListTransacoesService
        implements ListTransacoesUseCase {

    private final TransacaoRepositoryPort transacaoRepositoryPort;

    public ListTransacoesService(
            TransacaoRepositoryPort transacaoRepositoryPort) {

        this.transacaoRepositoryPort =
                transacaoRepositoryPort;
    }

    @Override
    public List<TransacaoResponse> listar(

            UUID usuarioId,

            LocalDateTime dataInicio,

            LocalDateTime dataFim,

            TipoTransacao tipo) {

        List<Transacao> transacoes;

        if (dataInicio != null && dataFim != null) {

            transacoes =
                    transacaoRepositoryPort.listarPorPeriodo(
                            usuarioId,
                            dataInicio,
                            dataFim
                    );

        } else {

            transacoes =
                    transacaoRepositoryPort.listarPorUsuario(
                            usuarioId
                    );
        }

        if (tipo != null) {

            transacoes =
                    transacoes.stream()
                            .filter(transacao ->
                                    transacao.getTipo() == tipo)
                            .toList();
        }

        return transacoes.stream()
                .map(this::toResponse)
                .toList();
    }

    private TransacaoResponse toResponse(
            Transacao transacao) {

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