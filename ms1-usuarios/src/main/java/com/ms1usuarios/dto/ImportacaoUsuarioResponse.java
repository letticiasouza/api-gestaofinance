package com.ms1usuarios.dto;

public class ImportacaoUsuarioResponse {

    private int usuariosImportados;

    private int erros;

    public ImportacaoUsuarioResponse() {
    }

    public ImportacaoUsuarioResponse(
            int usuariosImportados,
            int erros) {

        this.usuariosImportados = usuariosImportados;
        this.erros = erros;
    }

    public int getUsuariosImportados() {
        return usuariosImportados;
    }

    public void setUsuariosImportados(int usuariosImportados) {
        this.usuariosImportados = usuariosImportados;
    }

    public int getErros() {
        return erros;
    }

    public void setErros(int erros) {
        this.erros = erros;
    }
}
