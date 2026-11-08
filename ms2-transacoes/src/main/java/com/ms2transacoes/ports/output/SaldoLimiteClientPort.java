package com.ms2transacoes.ports.output;

import java.math.BigDecimal;
import java.util.UUID;

public interface SaldoLimiteClientPort {

    BigDecimal consultarSaldo(UUID usuarioId);

    BigDecimal consultarLimitePorTransacao(UUID usuarioId);

    BigDecimal consultarLimiteDiario(UUID usuarioId);
}