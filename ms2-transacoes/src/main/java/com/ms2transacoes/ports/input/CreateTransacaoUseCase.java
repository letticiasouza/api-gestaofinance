package com.ms2transacoes.ports.input;

import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.TransacaoResponse;

public interface CreateTransacaoUseCase {

    TransacaoResponse criar(
            CreateTransacaoRequest request);

}