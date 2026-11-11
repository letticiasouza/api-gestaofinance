package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.ResumoCategoriaResponse;
import com.ms2transacoes.dto.ResumoDespesasResponse;
import com.ms2transacoes.infrastructure.UsuarioAutenticadoProvider;
import com.ms2transacoes.ports.input.ResumoDespesasUseCase;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResumoDespesasService
        implements ResumoDespesasUseCase {

    private final TransacaoRepositoryPort
            transacaoRepositoryPort;

    private final UsuarioAutenticadoProvider
            usuarioAutenticadoProvider;

    public ResumoDespesasService(
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
    public ResumoDespesasResponse resumir(
            LocalDate dataInicio,
            LocalDate dataFim) {

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

        List<Transacao> transacoes =
                transacaoRepositoryPort
                        .listarPorPeriodo(
                                usuarioId,
                                dataInicio.atStartOfDay(),
                                dataFim.atTime(
                                        23,
                                        59,
                                        59
                                )
                        );

        List<Transacao> despesas =
                transacoes.stream()
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

        BigDecimal totalGeral =
                despesas.stream()
                        .map(
                                Transacao::getValor
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        Map<String, BigDecimal> agrupado =
                despesas.stream()
                        .collect(
                                Collectors.groupingBy(
                                        transacao ->
                                                transacao.getCategoria()
                                                        .name(),
                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Transacao::getValor,
                                                BigDecimal::add
                                        )
                                )
                        );

        List<ResumoCategoriaResponse> categorias =
                agrupado.entrySet()
                        .stream()
                        .map(
                                entry ->
                                        new ResumoCategoriaResponse(
                                                entry.getKey(),
                                                entry.getValue()
                                        )
                        )
                        .toList();

        return new ResumoDespesasResponse(
                totalGeral,
                categorias
        );
    }
}