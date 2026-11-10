package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.exception.TransacaoNaoEncontradaException;
import com.ms2transacoes.domain.model.CategoriaDespesa;
import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.output.CambioClientPort;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTransacaoServiceTest {

    @Mock
    private TransacaoRepositoryPort transacaoRepositoryPort;

    @Mock
    private CambioClientPort cambioClientPort;

    @InjectMocks
    private FindTransacaoService findTransacaoService;

    @Test
    void deveBuscarTransacaoPorIdComSucesso() {

        UUID id = UUID.randomUUID();

        Transacao transacao = new Transacao(
                id,
                TipoTransacao.DEPOSITO,
                StatusTransacao.APROVADA,
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                "Deposito teste",
                CategoriaDespesa.OUTROS,
                UUID.randomUUID(),
                null,
                null
        );

        when(transacaoRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.of(transacao));

        when(cambioClientPort.buscarUsd())
                .thenReturn(
                        new CambioClientPort.Cotacao(
                                "USD",
                                new BigDecimal("5.50"),
                                LocalDate.now()
                        )
                );

        when(cambioClientPort.buscarEur())
                .thenReturn(
                        new CambioClientPort.Cotacao(
                                "EUR",
                                new BigDecimal("6.00"),
                                LocalDate.now()
                        )
                );

        TransacaoResponse response =
                findTransacaoService.buscarPorId(id);

        assertNotNull(response);

        assertEquals(id, response.getId());

        assertEquals(
                TipoTransacao.DEPOSITO,
                response.getTipo());

        assertEquals(
                StatusTransacao.APROVADA,
                response.getStatus());
    }

    @Test
    void deveLancarExcecaoQuandoTransacaoNaoExistir() {

        UUID id = UUID.randomUUID();

        when(transacaoRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.empty());

        assertThrows(
                TransacaoNaoEncontradaException.class,
                () -> findTransacaoService.buscarPorId(id)
        );
    }
}