package com.ms2transacoes.adapters.output.persistence;

import com.ms2transacoes.domain.model.CategoriaDespesa;
import com.ms2transacoes.domain.model.StatusTransacao;
import com.ms2transacoes.domain.model.TipoTransacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacoes")
public class TransacaoEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    private BigDecimal valor;

    private LocalDateTime dataHora;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private CategoriaDespesa categoria;

    private UUID usuarioOrigemId;

    private UUID usuarioDestinoId;

    private String motivoRejeicao;

    public TransacaoEntity() {
    }

    public TransacaoEntity(
            UUID id,
            TipoTransacao tipo,
            StatusTransacao status,
            BigDecimal valor,
            LocalDateTime dataHora,
            String descricao,
            CategoriaDespesa categoria,
            UUID usuarioOrigemId,
            UUID usuarioDestinoId,
            String motivoRejeicao) {

        this.id = id;
        this.tipo = tipo;
        this.status = status;
        this.valor = valor;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.categoria = categoria;
        this.usuarioOrigemId = usuarioOrigemId;
        this.usuarioDestinoId = usuarioDestinoId;
        this.motivoRejeicao = motivoRejeicao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaDespesa getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }

    public UUID getUsuarioOrigemId() {
        return usuarioOrigemId;
    }

    public void setUsuarioOrigemId(UUID usuarioOrigemId) {
        this.usuarioOrigemId = usuarioOrigemId;
    }

    public UUID getUsuarioDestinoId() {
        return usuarioDestinoId;
    }

    public void setUsuarioDestinoId(UUID usuarioDestinoId) {
        this.usuarioDestinoId = usuarioDestinoId;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }
}