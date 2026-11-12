package com.ms2transacoes.domain.exception;

public class SaldoInsuficienteException
        extends RuntimeException {

    public SaldoInsuficienteException() {

        super(
                "Saldo insuficiente para realizar a transação."
        );
    }
}
