package com.ms2transacoes.ports.input;

import com.ms2transacoes.dto.SaldoResponse;

import java.util.UUID;

public interface GetSaldoUseCase {

    SaldoResponse consultarSaldo(
            UUID usuarioId
    );
}