package com.ms2transacoes.application.service;

import com.ms2transacoes.dto.ContaMockApiResponse;
import com.ms2transacoes.dto.SaldoResponse;
import com.ms2transacoes.infrastructure.UsuarioAutenticadoProvider;
import com.ms2transacoes.ports.input.GetSaldoUseCase;
import com.ms2transacoes.ports.output.SaldoLimiteClientPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GetSaldoService
        implements GetSaldoUseCase {

    private final SaldoLimiteClientPort
            saldoLimiteClientPort;

    private final UsuarioAutenticadoProvider
            usuarioAutenticadoProvider;

    public GetSaldoService(
            SaldoLimiteClientPort saldoLimiteClientPort,
            UsuarioAutenticadoProvider
                    usuarioAutenticadoProvider) {

        this.saldoLimiteClientPort =
                saldoLimiteClientPort;

        this.usuarioAutenticadoProvider =
                usuarioAutenticadoProvider;
    }

    @Override
    public SaldoResponse consultarSaldo() {

        var usuarioId =
                usuarioAutenticadoProvider
                        .getId();

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