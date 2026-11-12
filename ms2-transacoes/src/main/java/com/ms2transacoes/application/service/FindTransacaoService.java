package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.exception.AcessoNegadoException;
import com.ms2transacoes.domain.exception.TransacaoNaoEncontradaException;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.infrastructure.UsuarioAutenticadoProvider;
import com.ms2transacoes.ports.input.FindTransacaoUseCase;
import com.ms2transacoes.ports.output.CambioClientPort;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class FindTransacaoService
        implements FindTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepositoryPort;

    private final CambioClientPort cambioClientPort;

    private final UsuarioAutenticadoProvider
            usuarioAutenticadoProvider;

    public FindTransacaoService(
            TransacaoRepositoryPort transacaoRepositoryPort,
            CambioClientPort cambioClientPort,
            UsuarioAutenticadoProvider
                    usuarioAutenticadoProvider) {

        this.transacaoRepositoryPort =
                transacaoRepositoryPort;

        this.cambioClientPort =
                cambioClientPort;

        this.usuarioAutenticadoProvider =
                usuarioAutenticadoProvider;
    }

    @Override
    public TransacaoResponse buscarPorId(
            UUID id) {

        Transacao transacao =
                transacaoRepositoryPort
                        .buscarPorId(id)
                        .orElseThrow(() ->
                                new TransacaoNaoEncontradaException(id));

        UUID usuarioLogadoId =
                usuarioAutenticadoProvider
                        .getId();

        if (!transacao.getUsuarioOrigemId()
                .equals(usuarioLogadoId)) {

            throw new AcessoNegadoException();
        }

        TransacaoResponse response =
                new TransacaoResponse(
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

        CambioClientPort.Cotacao usd =
                cambioClientPort.buscarUsd();

        CambioClientPort.Cotacao eur =
                cambioClientPort.buscarEur();

        if (usd.taxa() != null
                && eur.taxa() != null) {

            response.setTaxaUsd(
                    usd.taxa()
            );

            response.setTaxaEur(
                    eur.taxa()
            );

            response.setDataCotacao(
                    usd.data()
            );

            response.setValorUsd(
                    transacao.getValor()
                            .divide(
                                    usd.taxa(),
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                            )
            );

            response.setValorEur(
                    transacao.getValor()
                            .divide(
                                    eur.taxa(),
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                            )
            );

        } else {

            response.setMensagemConversao(
                    "Conversão não disponível no momento."
            );
        }

        return response;
    }
}