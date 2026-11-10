package com.ms2transacoes.ports.output;

import com.ms2transacoes.dto.ContaMockApiResponse;

import java.math.BigDecimal;

public interface SaldoLimiteClientPort {

    ContaMockApiResponse buscarContaPorUsuario(
            String usuarioId
    );

    void atualizarSaldo(
            String usuarioId,
            BigDecimal novoSaldo
    );
}