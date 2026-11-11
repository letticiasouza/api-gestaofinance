package com.ms2transacoes.ports.output;

import com.ms2transacoes.dto.UsuarioAuthResponse;

public interface UsuarioClientPort {

    UsuarioAuthResponse buscarPorEmail(
            String email
    );

}