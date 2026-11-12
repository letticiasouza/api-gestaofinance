package com.ms1usuarios.ports.input;

import com.ms1usuarios.dto.UsuarioAuthResponse;

public interface FindUsuarioAuthUseCase {

    UsuarioAuthResponse buscarPorEmail(
            String email
    );

}