package com.ms1usuarios.ports.input;

import com.ms1usuarios.dto.UpdateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;

import java.util.UUID;

public interface UpdateUsuarioUseCase {

    UsuarioResponse atualizar(UUID id, UpdateUsuarioRequest request);

}