package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.exception.ContaNaoEncontradaException;
import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.ContaMockApiResponse;
import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.infrastructure.UsuarioAutenticadoProvider;
import com.ms2transacoes.ports.input.CreateTransacaoUseCase;
import com.ms2transacoes.ports.output.SaldoLimiteClientPort;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class CreateTransacaoService
        implements CreateTransacaoUseCase {

    private static final ZoneId
            ZONA_SAO_PAULO =
            ZoneId.of(
                    "America/Sao_Paulo"
            );

    private final TransacaoRepositoryPort transacaoRepositoryPort;

    private final SaldoLimiteClientPort saldoLimiteClientPort;

    private final UsuarioAutenticadoProvider
            usuarioAutenticadoProvider;

    public CreateTransacaoService(
            TransacaoRepositoryPort transacaoRepositoryPort,
            SaldoLimiteClientPort saldoLimiteClientPort,
            UsuarioAutenticadoProvider usuarioAutenticadoProvider) {

        this.transacaoRepositoryPort =
                transacaoRepositoryPort;

        this.saldoLimiteClientPort =
                saldoLimiteClientPort;

        this.usuarioAutenticadoProvider =
                usuarioAutenticadoProvider;
    }

    @Override
    public TransacaoResponse criar(
            CreateTransacaoRequest request) {

        UUID usuarioId =
                usuarioAutenticadoProvider.getId();

        ContaMockApiResponse conta =
                saldoLimiteClientPort
                        .buscarContaPorUsuario(
                                usuarioId.toString()
                        );

        if (conta == null) {

            throw new ContaNaoEncontradaException();
        }

        ContaMockApiResponse contaDestino = null;

        if (request.getTipo()
                == TipoTransacao.TRANSFERENCIA) {

            if (request.getUsuarioDestinoId() == null) {

                return salvarRejeitada(
                        request,
                        usuarioId,
                        "Usuário destino é obrigatório"
                );
            }

            contaDestino =
                    saldoLimiteClientPort
                            .buscarContaPorUsuario(
                                    request.getUsuarioDestinoId()
                                            .toString()
                            );

            if (contaDestino == null) {

                return salvarRejeitada(
                        request,
                        usuarioId,
                        "Usuário destino não encontrado"
                );
            }
        }

        StatusTransacao status =
                StatusTransacao.APROVADA;

        String motivoRejeicao = null;

        if (request.getValor()
                .compareTo(conta.getSaldo()) > 0) {

            status = StatusTransacao.REJEITADA;

            motivoRejeicao =
                    "Saldo insuficiente";

        } else if (request.getValor()
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
                                    usuarioId
                            );

            LocalDate hoje =
                    LocalDate.now(
                            ZONA_SAO_PAULO
                    );

            BigDecimal totalDoDia =
                    transacoesDoUsuario.stream()
                            .filter(
                                    transacao ->
                                            transacao.getStatus()
                                                    == StatusTransacao.APROVADA
                            )
                            .filter(
                                    transacao ->
                                            transacao.getDataHora()
                                                    .toLocalDate()
                                                    .equals(hoje)
                            )
                            .map(
                                    Transacao::getValor
                            )
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

                status =
                        StatusTransacao.REJEITADA;

                motivoRejeicao =
                        "Limite diário excedido";
            }
        }

        if (status == StatusTransacao.REJEITADA) {

            return salvarRejeitada(
                    request,
                    usuarioId,
                    motivoRejeicao
            );
        }

        // Atualiza saldo ANTES de salvar a transação

        try {

            if (request.getTipo()
                    == TipoTransacao.DEPOSITO) {

                BigDecimal novoSaldo =
                        conta.getSaldo()
                                .add(
                                        request.getValor()
                                );

                saldoLimiteClientPort
                        .atualizarSaldo(
                                usuarioId.toString(),
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
                                usuarioId.toString(),
                                novoSaldoOrigem
                        );

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

            } else {

                BigDecimal novoSaldo =
                        conta.getSaldo()
                                .subtract(
                                        request.getValor()
                                );

                saldoLimiteClientPort
                        .atualizarSaldo(
                                usuarioId.toString(),
                                novoSaldo
                        );
            }

        } catch (Exception e) {

            return salvarRejeitada(
                    request,
                    usuarioId,
                    "Serviço de saldo indisponível"
            );
        }

        Transacao transacao = new Transacao(
                UUID.randomUUID(),
                request.getTipo(),
                StatusTransacao.APROVADA,
                request.getValor(),
                LocalDateTime.now(),
                request.getDescricao(),
                request.getCategoria(),
                usuarioId,
                request.getUsuarioDestinoId(),
                null
        );

        Transacao transacaoSalva =
                transacaoRepositoryPort
                        .salvar(transacao);

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

    private TransacaoResponse salvarRejeitada(
            CreateTransacaoRequest request,
            UUID usuarioId,
            String motivo) {

        Transacao transacao =
                new Transacao(
                        UUID.randomUUID(),
                        request.getTipo(),
                        StatusTransacao.REJEITADA,
                        request.getValor(),
                        LocalDateTime.now(),
                        request.getDescricao(),
                        request.getCategoria(),
                        usuarioId,
                        request.getUsuarioDestinoId(),
                        motivo
                );

        Transacao salva =
                transacaoRepositoryPort
                        .salvar(transacao);

        return new TransacaoResponse(
                salva.getId(),
                salva.getTipo(),
                salva.getStatus(),
                salva.getValor(),
                salva.getDataHora(),
                salva.getDescricao(),
                salva.getCategoria(),
                salva.getUsuarioOrigemId(),
                salva.getUsuarioDestinoId(),
                salva.getMotivoRejeicao()
        );
    }
}