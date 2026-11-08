package com.ms2transacoes.ports.input;

import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.dto.TransacaoResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ListTransacoesUseCase {

    List<TransacaoResponse> listar(

            UUID usuarioId,

            LocalDateTime dataInicio,

            LocalDateTime dataFim,

            TipoTransacao tipo
    );
}