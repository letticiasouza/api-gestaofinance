package com.ms1usuarios.ports.input;

import com.ms1usuarios.dto.UsuarioResponse;

import java.util.List;

public interface ListUsuariosUseCase {

    List<UsuarioResponse> listarTodos();

}