package com.ms1usuarios.dto;

import java.util.UUID;

public class LoginResponse {

    private String mensagem;

    private UUID id;

    private String nome;

    private String email;

    public LoginResponse() {
    }

    public LoginResponse(
            String mensagem,
            UUID id,
            String nome,
            String email) {

        this.mensagem = mensagem;
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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