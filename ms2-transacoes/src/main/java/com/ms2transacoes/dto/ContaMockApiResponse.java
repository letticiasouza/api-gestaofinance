package com.ms2transacoes.dto;

import java.math.BigDecimal;

public class ContaMockApiResponse {

    private String id;

    private String usuarioId;

    private BigDecimal saldo;

    private BigDecimal limiteDiario;

    private BigDecimal limitePorTransacao;

    public ContaMockApiResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getLimiteDiario() {
        return limiteDiario;
    }

    public void setLimiteDiario(BigDecimal limiteDiario) {
        this.limiteDiario = limiteDiario;
    }

    public BigDecimal getLimitePorTransacao() {
        return limitePorTransacao;
    }

    public void setLimitePorTransacao(
            BigDecimal limitePorTransacao) {

        this.limitePorTransacao =
                limitePorTransacao;
    }
}