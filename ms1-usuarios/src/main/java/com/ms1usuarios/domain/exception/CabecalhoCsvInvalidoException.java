package com.ms1usuarios.domain.exception;

public class CabecalhoCsvInvalidoException
        extends RuntimeException {

    public CabecalhoCsvInvalidoException() {

        super(
                "Cabeçalho inválido. Esperado: nome,email,senha"
        );
    }
}