package com.ms1usuarios.ports.input;

import com.ms1usuarios.dto.UsuarioResponse;

import java.util.UUID;

public interface FindUsuarioUseCase {

    UsuarioResponse buscarPorId(UUID id);

}