package com.ms2transacoes.domain.exception;

public class ServicoSaldoIndisponivelException
        extends RuntimeException {

    public ServicoSaldoIndisponivelException() {

        super(
                "Não foi possível consultar saldo e limites no serviço externo."
        );
    }
}