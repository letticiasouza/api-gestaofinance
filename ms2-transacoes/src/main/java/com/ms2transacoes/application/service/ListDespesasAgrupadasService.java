package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.DespesaAgrupadaResponse;
import com.ms2transacoes.dto.DespesasAgrupadasResponse;
import com.ms2transacoes.dto.ModoAnalise;
import com.ms2transacoes.infrastructure.UsuarioAutenticadoProvider;
import com.ms2transacoes.ports.input.ListDespesasAgrupadasUseCase;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ListDespesasAgrupadasService
        implements ListDespesasAgrupadasUseCase {

    private final TransacaoRepositoryPort
            transacaoRepositoryPort;

    private final UsuarioAutenticadoProvider
            usuarioAutenticadoProvider;

    public ListDespesasAgrupadasService(
            TransacaoRepositoryPort
                    transacaoRepositoryPort,
            UsuarioAutenticadoProvider
                    usuarioAutenticadoProvider) {

        this.transacaoRepositoryPort =
                transacaoRepositoryPort;

        this.usuarioAutenticadoProvider =
                usuarioAutenticadoProvider;
    }

    @Override
    public DespesasAgrupadasResponse listar(
            LocalDate dataInicio,
            LocalDate dataFim,
            ModoAnalise modo) {

        if (modo == null
                || modo == ModoAnalise.NENHUM) {

            throw new IllegalArgumentException(
                    "Informe o modo da análise."
            );
        }

        var usuarioId =
                usuarioAutenticadoProvider
                        .getId();

        if (dataInicio == null || dataFim == null) {

            YearMonth mesAtual =
                    YearMonth.now();

            dataInicio =
                    mesAtual.atDay(1);

            dataFim =
                    mesAtual.atEndOfMonth();
        }

        List<Transacao> despesas =
                transacaoRepositoryPort
                        .listarPorPeriodo(
                                usuarioId,
                                dataInicio.atStartOfDay(),
                                dataFim.atTime(
                                        23,
                                        59,
                                        59
                                )
                        )
                        .stream()
                        .filter(
                                transacao ->
                                        transacao.getStatus()
                                                == StatusTransacao.APROVADA
                        )
                        .filter(
                                transacao ->
                                        transacao.getTipo()
                                                == TipoTransacao.COMPRA
                                                || transacao.getTipo()
                                                == TipoTransacao.RETIRADA
                                                || transacao.getTipo()
                                                == TipoTransacao.TRANSFERENCIA
                        )
                        .toList();

        Map<String, BigDecimal> agrupado;

        if (modo == ModoAnalise.DIA) {

            agrupado =
                    despesas.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            transacao ->
                                                    transacao.getDataHora()
                                                            .toLocalDate()
                                                            .toString(),
                                            Collectors.reducing(
                                                    BigDecimal.ZERO,
                                                    Transacao::getValor,
                                                    BigDecimal::add
                                            )
                                    )
                            );

        } else {

            agrupado =
                    despesas.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            transacao ->
                                                    YearMonth.from(
                                                            transacao.getDataHora()
                                                    ).toString(),
                                            Collectors.reducing(
                                                    BigDecimal.ZERO,
                                                    Transacao::getValor,
                                                    BigDecimal::add
                                            )
                                    )
                            );
        }

        List<DespesaAgrupadaResponse> dados =
                agrupado.entrySet()
                        .stream()
                        .map(
                                entry ->
                                        new DespesaAgrupadaResponse(
                                                entry.getKey(),
                                                entry.getValue()
                                        )
                        )
                        .toList();

        return new DespesasAgrupadasResponse(
                modo,
                dados
        );
    }
}