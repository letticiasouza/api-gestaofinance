package com.ms2transacoes.ports.input;

import com.ms2transacoes.dto.ResumoDespesasResponse;

import java.time.LocalDate;

public interface ResumoDespesasUseCase {

    ResumoDespesasResponse resumir(
            LocalDate dataInicio,
            LocalDate dataFim
    );

}