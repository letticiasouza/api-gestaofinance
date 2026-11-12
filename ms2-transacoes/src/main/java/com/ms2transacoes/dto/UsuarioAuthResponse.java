package com.ms2transacoes.dto;

import java.util.UUID;

public class UsuarioAuthResponse {

    private UUID id;

    private String nome;

    private String email;

    public UsuarioAuthResponse() {
    }

    public UsuarioAuthResponse(
            UUID id,
            String nome,
            String email) {

        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}