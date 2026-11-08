package com.ms2transacoes.domain.exception;

public class LimiteTransacaoExcedidoException
        extends RuntimeException {

    public LimiteTransacaoExcedidoException() {

        super(
                "Valor excede o limite permitido para a transação."
        );
    }
}