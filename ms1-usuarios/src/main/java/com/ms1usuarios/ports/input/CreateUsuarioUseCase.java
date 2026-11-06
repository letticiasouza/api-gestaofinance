package com.ms1usuarios.ports.input;

import com.ms1usuarios.dto.CreateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;

public interface CreateUsuarioUseCase {

    UsuarioResponse criar(CreateUsuarioRequest request);

}
