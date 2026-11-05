package com.ms1usuarios.domain.exception;

public class LoginInvalidoException extends RuntimeException {

    public LoginInvalidoException() {
        super("Email ou senha inválidos");
    }

}