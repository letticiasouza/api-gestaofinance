package com.ms2transacoes.domain.exception;

public class ContaNaoEncontradaException
        extends RuntimeException {

    public ContaNaoEncontradaException() {

        super(
                "Conta não encontrada no serviço externo."
        );
    }
}