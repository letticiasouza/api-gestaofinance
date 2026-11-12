package com.ms2transacoes.dto;

import java.math.BigDecimal;

public class DespesaAgrupadaResponse {

    private String referencia;

    private BigDecimal total;

    public DespesaAgrupadaResponse() {
    }

    public DespesaAgrupadaResponse(
            String referencia,
            BigDecimal total) {

        this.referencia = referencia;
        this.total = total;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(
            String referencia) {

        this.referencia = referencia;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(
            BigDecimal total) {

        this.total = total;
    }
}