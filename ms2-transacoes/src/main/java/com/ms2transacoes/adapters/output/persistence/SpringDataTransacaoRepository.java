package com.ms2transacoes.adapters.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SpringDataTransacaoRepository
        extends JpaRepository<TransacaoEntity, UUID> {

    List<TransacaoEntity> findByUsuarioOrigemId(
            UUID usuarioOrigemId
    );

    List<TransacaoEntity>
    findByUsuarioOrigemIdAndDataHoraBetween(
            UUID usuarioOrigemId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );
}