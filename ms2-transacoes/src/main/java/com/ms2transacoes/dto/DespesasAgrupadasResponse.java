package com.ms2transacoes.dto;

import java.util.List;

public class DespesasAgrupadasResponse {

    private ModoAnalise modo;

    private List<DespesaAgrupadaResponse> dados;

    public DespesasAgrupadasResponse() {
    }

    public DespesasAgrupadasResponse(
            ModoAnalise modo,
            List<DespesaAgrupadaResponse> dados) {

        this.modo = modo;
        this.dados = dados;
    }

    public ModoAnalise getModo() {
        return modo;
    }

    public void setModo(
            ModoAnalise modo) {

        this.modo = modo;
    }

    public List<DespesaAgrupadaResponse> getDados() {
        return dados;
    }

    public void setDados(
            List<DespesaAgrupadaResponse> dados) {

        this.dados = dados;
    }
}