package com.ms2transacoes.ports.input;

import com.ms2transacoes.dto.DespesasAgrupadasResponse;
import com.ms2transacoes.dto.ModoAnalise;

import java.time.LocalDate;

public interface ListDespesasAgrupadasUseCase {

    DespesasAgrupadasResponse listar(
            LocalDate dataInicio,
            LocalDate dataFim,
            ModoAnalise modo
    );

}