package com.ms2transacoes.ports.output;

import com.ms2transacoes.dto.LoginRequest;
import com.ms2transacoes.dto.LoginResponse;

public interface UsuarioClientPort {

    LoginResponse autenticar(
            LoginRequest request
    );

}