package com.ms2transacoes.dto;

import com.ms2transacoes.domain.model.CategoriaDespesa;
import com.ms2transacoes.domain.model.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateTransacaoRequest {

    @NotNull(message = "Tipo é obrigatório")
    private TipoTransacao tipo;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    private String descricao;

    private CategoriaDespesa categoria;

    @NotNull(message = "Usuário origem é obrigatório")
    private UUID usuarioOrigemId;

    private UUID usuarioDestinoId;

    public CreateTransacaoRequest() {
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
}