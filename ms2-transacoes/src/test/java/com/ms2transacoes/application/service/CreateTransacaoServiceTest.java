package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.CategoriaDespesa;
import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.ContaMockApiResponse;
import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.output.SaldoLimiteClientPort;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTransacaoServiceTest {

    @Mock
    private TransacaoRepositoryPort transacaoRepositoryPort;

    @Mock
    private SaldoLimiteClientPort saldoLimiteClientPort;

    @InjectMocks
    private CreateTransacaoService createTransacaoService;

    @Test
    void deveCriarTransacaoComSucesso() {

        CreateTransacaoRequest request =
                new CreateTransacaoRequest();

        request.setTipo(
                TipoTransacao.DEPOSITO);

        request.setValor(
                new BigDecimal("100"));

        request.setDescricao(
                "Deposito teste");

        request.setCategoria(
                CategoriaDespesa.OUTROS);

        request.setUsuarioOrigemId(
                UUID.randomUUID());

        request.setUsuarioDestinoId(
                UUID.randomUUID());

        ContaMockApiResponse conta =
                new ContaMockApiResponse();

        conta.setId("1");

        conta.setUsuarioId(
                request.getUsuarioOrigemId()
                        .toString()
        );

        conta.setSaldo(
                new BigDecimal("1000"));

        conta.setLimiteDiario(
                new BigDecimal("5000"));

        conta.setLimitePorTransacao(
                new BigDecimal("1000"));

        when(
                saldoLimiteClientPort
                        .buscarContaPorUsuario(
                                request
                                        .getUsuarioOrigemId()
                                        .toString()
                        )
        ).thenReturn(conta);

        Transacao transacaoSalva =
                new Transacao(
                        UUID.randomUUID(),
                        TipoTransacao.DEPOSITO,
                        StatusTransacao.APROVADA,
                        new BigDecimal("100"),
                        LocalDateTime.now(),
                        "Deposito teste",
                        CategoriaDespesa.OUTROS,
                        request.getUsuarioOrigemId(),
                        request.getUsuarioDestinoId(),
                        null
                );

        when(
                transacaoRepositoryPort.salvar(
                        any(Transacao.class)
                )
        ).thenReturn(transacaoSalva);

        TransacaoResponse response =
                createTransacaoService.criar(
                        request
                );

        assertNotNull(response);

        assertEquals(
                TipoTransacao.DEPOSITO,
                response.getTipo());

        assertEquals(
                StatusTransacao.APROVADA,
                response.getStatus());

        assertEquals(
                new BigDecimal("100"),
                response.getValor());
    }

    @Test
    void deveRejeitarTransacaoQuandoSaldoForInsuficiente() {

        CreateTransacaoRequest request =
                new CreateTransacaoRequest();

        request.setTipo(
                TipoTransacao.COMPRA);

        request.setValor(
                new BigDecimal("100"));

        request.setDescricao(
                "Compra teste");

        request.setCategoria(
                CategoriaDespesa.ALIMENTACAO);

        request.setUsuarioOrigemId(
                UUID.randomUUID());

        ContaMockApiResponse conta =
                new ContaMockApiResponse();

        conta.setId("1");

        conta.setUsuarioId(
                request.getUsuarioOrigemId()
                        .toString()
        );

        conta.setSaldo(
                new BigDecimal("67"));

        conta.setLimiteDiario(
                new BigDecimal("99"));

        conta.setLimitePorTransacao(
                new BigDecimal("10"));

        when(
                saldoLimiteClientPort
                        .buscarContaPorUsuario(
                                request
                                        .getUsuarioOrigemId()
                                        .toString()
                        )
        ).thenReturn(conta);

        Transacao transacaoRejeitada =
                new Transacao(
                        UUID.randomUUID(),
                        TipoTransacao.COMPRA,
                        StatusTransacao.REJEITADA,
                        new BigDecimal("100"),
                        LocalDateTime.now(),
                        "Compra teste",
                        CategoriaDespesa.ALIMENTACAO,
                        request.getUsuarioOrigemId(),
                        null,
                        "Saldo insuficiente"
                );

        when(
                transacaoRepositoryPort.salvar(
                        any(Transacao.class)
                )
        ).thenReturn(transacaoRejeitada);

        TransacaoResponse response =
                createTransacaoService.criar(
                        request
                );

        assertEquals(
                StatusTransacao.REJEITADA,
                response.getStatus()
        );

        assertEquals(
                "Saldo insuficiente",
                response.getMotivoRejeicao()
        );
    }
}