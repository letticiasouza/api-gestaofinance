package com.ms2transacoes.dto;

import java.math.BigDecimal;
import java.util.List;

public class ResumoDespesasResponse {

    private BigDecimal totalGeral;

    private List<ResumoCategoriaResponse> categorias;

    public ResumoDespesasResponse() {
    }

    public ResumoDespesasResponse(
            BigDecimal totalGeral,
            List<ResumoCategoriaResponse> categorias) {

        this.totalGeral = totalGeral;
        this.categorias = categorias;
    }

    public BigDecimal getTotalGeral() {
        return totalGeral;
    }

    public void setTotalGeral(BigDecimal totalGeral) {
        this.totalGeral = totalGeral;
    }

    public List<ResumoCategoriaResponse> getCategorias() {
        return categorias;
    }

    public void setCategorias(
            List<ResumoCategoriaResponse> categorias) {

        this.categorias = categorias;
    }
}