package com.ms2transacoes.ports.input;

import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.dto.TransacaoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ListTransacoesUseCase {

    List<TransacaoResponse> listar(

            LocalDateTime dataInicio,

            LocalDateTime dataFim,

            TipoTransacao tipo
    );
}