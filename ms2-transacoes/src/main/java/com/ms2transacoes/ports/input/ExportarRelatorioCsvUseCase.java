package com.ms2transacoes.ports.input;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface ExportarRelatorioCsvUseCase {

    ResponseEntity<byte[]> exportar(
            LocalDate dataInicio,
            LocalDate dataFim
    );

}
