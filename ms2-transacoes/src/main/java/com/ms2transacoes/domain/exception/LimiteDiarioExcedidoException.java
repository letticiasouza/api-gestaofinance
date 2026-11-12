package com.ms2transacoes.domain.exception;

public class LimiteDiarioExcedidoException
        extends RuntimeException {

    public LimiteDiarioExcedidoException() {

        super(
                "Limite diário excedido."
        );
    }
}