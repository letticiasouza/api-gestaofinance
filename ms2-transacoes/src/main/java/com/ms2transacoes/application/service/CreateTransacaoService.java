package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.exception.ContaNaoEncontradaException;
import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.ContaMockApiResponse;
import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.input.CreateTransacaoUseCase;
import com.ms2transacoes.ports.output.SaldoLimiteClientPort;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CreateTransacaoService
        implements CreateTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepositoryPort;

    private final SaldoLimiteClientPort saldoLimiteClientPort;

    public CreateTransacaoService(
            TransacaoRepositoryPort transacaoRepositoryPort,
            SaldoLimiteClientPort saldoLimiteClientPort) {

        this.transacaoRepositoryPort =
                transacaoRepositoryPort;

        this.saldoLimiteClientPort =
                saldoLimiteClientPort;
    }

    @Override
    public TransacaoResponse criar(
            CreateTransacaoRequest request) {

        ContaMockApiResponse conta =
                saldoLimiteClientPort
                        .buscarContaPorUsuario(
                                request
                                        .getUsuarioOrigemId()
                                        .toString()
                        );

        if (conta == null) {

            throw new ContaNaoEncontradaException();
        }

        StatusTransacao status =
                StatusTransacao.APROVADA;

        String motivoRejeicao = null;

        if (request.getValor()
                .compareTo(conta.getSaldo()) > 0) {

            status = StatusTransacao.REJEITADA;

            motivoRejeicao =
                    "Saldo insuficiente";

        } else if (

                request.getValor()
                        .compareTo(
                                conta.getLimitePorTransacao()
                        ) > 0) {

            status = StatusTransacao.REJEITADA;

            motivoRejeicao =
                    "Limite por transação excedido";

        } else {

            List<Transacao> transacoesDoUsuario =
                    transacaoRepositoryPort
                            .listarPorUsuario(
                                    request.getUsuarioOrigemId()
                            );

            LocalDate hoje =
                    LocalDate.now();

            BigDecimal totalDoDia =
                    transacoesDoUsuario.stream()
                            .filter(transacao ->
                                    transacao.getStatus()
                                            == StatusTransacao.APROVADA)
                            .filter(transacao ->
                                    transacao.getDataHora()
                                            .toLocalDate()
                                            .equals(hoje))
                            .map(Transacao::getValor)
                            .reduce(
                                    BigDecimal.ZERO,
                                    BigDecimal::add
                            );

            BigDecimal totalComNovaTransacao =
                    totalDoDia.add(
                            request.getValor()
                    );

            if (totalComNovaTransacao.compareTo(
                    conta.getLimiteDiario()
            ) > 0) {

                status = StatusTransacao.REJEITADA;

                motivoRejeicao =
                        "Limite diário excedido";
            }
        }

        Transacao transacao = new Transacao(
                UUID.randomUUID(),
                request.getTipo(),
                status,
                request.getValor(),
                LocalDateTime.now(),
                request.getDescricao(),
                request.getCategoria(),
                request.getUsuarioOrigemId(),
                request.getUsuarioDestinoId(),
                motivoRejeicao
        );

        Transacao transacaoSalva =
                transacaoRepositoryPort
                        .salvar(transacao);

        if (status == StatusTransacao.APROVADA) {

            if (request.getTipo()
                    == TipoTransacao.DEPOSITO) {

                BigDecimal novoSaldo =
                        conta.getSaldo()
                                .add(
                                        request.getValor()
                                );

                saldoLimiteClientPort
                        .atualizarSaldo(
                                request.getUsuarioOrigemId()
                                        .toString(),
                                novoSaldo
                        );

            } else if (request.getTipo()
                    == TipoTransacao.TRANSFERENCIA) {

                BigDecimal novoSaldoOrigem =
                        conta.getSaldo()
                                .subtract(
                                        request.getValor()
                                );

                saldoLimiteClientPort
                        .atualizarSaldo(
                                request.getUsuarioOrigemId()
                                        .toString(),
                                novoSaldoOrigem
                        );

                ContaMockApiResponse contaDestino =
                        saldoLimiteClientPort
                                .buscarContaPorUsuario(
                                        request.getUsuarioDestinoId()
                                                .toString()
                                );

                if (contaDestino != null) {

                    BigDecimal novoSaldoDestino =
                            contaDestino.getSaldo()
                                    .add(
                                            request.getValor()
                                    );

                    saldoLimiteClientPort
                            .atualizarSaldo(
                                    request.getUsuarioDestinoId()
                                            .toString(),
                                    novoSaldoDestino
                            );
                }

            } else {

                BigDecimal novoSaldo =
                        conta.getSaldo()
                                .subtract(
                                        request.getValor()
                                );

                saldoLimiteClientPort
                        .atualizarSaldo(
                                request.getUsuarioOrigemId()
                                        .toString(),
                                novoSaldo
                        );
            }
        }

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