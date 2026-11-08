package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.CategoriaDespesa;
import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTransacaoServiceTest {

    @Mock
    private TransacaoRepositoryPort transacaoRepositoryPort;

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

        when(transacaoRepositoryPort.salvar(
                any(Transacao.class)))
                .thenReturn(transacaoSalva);

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
}