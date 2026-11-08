package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.CategoriaDespesa;
import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTransacoesServiceTest {

    @Mock
    private TransacaoRepositoryPort transacaoRepositoryPort;

    @InjectMocks
    private ListTransacoesService listTransacoesService;

    @Test
    void deveListarTransacoesPorUsuario() {

        UUID usuarioId = UUID.randomUUID();

        Transacao transacao = new Transacao(
                UUID.randomUUID(),
                TipoTransacao.DEPOSITO,
                StatusTransacao.APROVADA,
                new BigDecimal("100"),
                LocalDateTime.now(),
                "Deposito",
                CategoriaDespesa.OUTROS,
                usuarioId,
                null,
                null
        );

        when(transacaoRepositoryPort
                .listarPorUsuario(usuarioId))
                .thenReturn(List.of(transacao));

        List<TransacaoResponse> response =
                listTransacoesService.listar(
                        usuarioId,
                        null,
                        null,
                        null
                );

        assertEquals(1, response.size());

        assertEquals(
                TipoTransacao.DEPOSITO,
                response.getFirst().getTipo()
        );
    }

    @Test
    void deveFiltrarPorTipo() {

        UUID usuarioId = UUID.randomUUID();

        Transacao deposito = new Transacao(
                UUID.randomUUID(),
                TipoTransacao.DEPOSITO,
                StatusTransacao.APROVADA,
                new BigDecimal("100"),
                LocalDateTime.now(),
                "Deposito",
                CategoriaDespesa.OUTROS,
                usuarioId,
                null,
                null
        );

        Transacao compra = new Transacao(
                UUID.randomUUID(),
                TipoTransacao.COMPRA,
                StatusTransacao.APROVADA,
                new BigDecimal("50"),
                LocalDateTime.now(),
                "Compra",
                CategoriaDespesa.ALIMENTACAO,
                usuarioId,
                null,
                null
        );

        when(transacaoRepositoryPort
                .listarPorUsuario(usuarioId))
                .thenReturn(List.of(
                        deposito,
                        compra
                ));

        List<TransacaoResponse> response =
                listTransacoesService.listar(
                        usuarioId,
                        null,
                        null,
                        TipoTransacao.COMPRA
                );

        assertEquals(1, response.size());

        assertEquals(
                TipoTransacao.COMPRA,
                response.getFirst().getTipo()
        );
    }

    @Test
    void deveRetornarListaVazia() {

        UUID usuarioId = UUID.randomUUID();

        when(transacaoRepositoryPort
                .listarPorUsuario(usuarioId))
                .thenReturn(List.of());

        List<TransacaoResponse> response =
                listTransacoesService.listar(
                        usuarioId,
                        null,
                        null,
                        null
                );

        assertTrue(response.isEmpty());
    }
}