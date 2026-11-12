package com.ms2transacoes.ports.output;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CambioClientPort {

    Cotacao buscarUsd();

    Cotacao buscarEur();

    record Cotacao(
            String moeda,
            BigDecimal taxa,
            LocalDate data
    ) {
    }
}