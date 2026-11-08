package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.input.CreateTransacaoUseCase;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateTransacaoService
        implements CreateTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepositoryPort;

    public CreateTransacaoService(
            TransacaoRepositoryPort transacaoRepositoryPort) {

        this.transacaoRepositoryPort = transacaoRepositoryPort;
    }

    @Override
    public TransacaoResponse criar(
            CreateTransacaoRequest request) {

        Transacao transacao = new Transacao(
                UUID.randomUUID(),
                request.getTipo(),
                StatusTransacao.APROVADA,
                request.getValor(),
                LocalDateTime.now(),
                request.getDescricao(),
                request.getCategoria(),
                request.getUsuarioOrigemId(),
                request.getUsuarioDestinoId(),
                null
        );

        Transacao transacaoSalva =
                transacaoRepositoryPort.salvar(transacao);

        return new TransacaoResponse(
                transacaoSalva.getId(),
                transacaoSalva.getTipo(),
                transacaoSalva.getStatus(),
                transacaoSalva.getValor(),
                transacaoSalva.getDataHora(),
                transacaoSalva.getDescricao(),
                transacaoSalva.getCategoria(),
                transacaoSalva.getUsuarioOrigemId(),
                transacaoSalva.getUsuarioDestinoId(),
                transacaoSalva.getMotivoRejeicao()
        );
    }
}