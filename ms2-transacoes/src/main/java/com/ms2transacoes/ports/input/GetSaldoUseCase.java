package com.ms2transacoes.ports.input;

import com.ms2transacoes.dto.SaldoResponse;

public interface GetSaldoUseCase {

    SaldoResponse consultarSaldo();

}