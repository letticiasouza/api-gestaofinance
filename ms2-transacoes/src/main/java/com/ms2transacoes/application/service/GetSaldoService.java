package com.ms2transacoes.application.service;

import com.ms2transacoes.dto.ContaMockApiResponse;
import com.ms2transacoes.dto.SaldoResponse;
import com.ms2transacoes.ports.input.GetSaldoUseCase;
import com.ms2transacoes.ports.output.SaldoLimiteClientPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class GetSaldoService
        implements GetSaldoUseCase {

    private final SaldoLimiteClientPort saldoLimiteClientPort;

    public GetSaldoService(
            SaldoLimiteClientPort saldoLimiteClientPort) {

        this.saldoLimiteClientPort =
                saldoLimiteClientPort;
    }

    @Override
    public SaldoResponse consultarSaldo(
            UUID usuarioId) {

        ContaMockApiResponse conta =
                saldoLimiteClientPort
                        .buscarContaPorUsuario(
                                usuarioId.toString()
                        );

        if (conta == null) {

            return new SaldoResponse(
                    BigDecimal.ZERO
            );
        }

        return new SaldoResponse(
                conta.getSaldo()
        );
    }
}