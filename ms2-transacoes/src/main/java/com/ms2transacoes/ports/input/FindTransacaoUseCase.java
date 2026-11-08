package com.ms2transacoes.ports.input;

import com.ms2transacoes.dto.TransacaoResponse;

import java.util.UUID;

public interface FindTransacaoUseCase {

    TransacaoResponse buscarPorId(UUID id);

}