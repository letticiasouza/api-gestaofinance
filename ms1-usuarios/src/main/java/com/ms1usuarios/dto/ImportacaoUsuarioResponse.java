package com.ms1usuarios.dto;

import java.util.ArrayList;
import java.util.List;

public class ImportacaoUsuarioResponse {

    private int usuariosImportados;

    private int usuariosComErro;

    private List<ErroImportacao> erros =
            new ArrayList<>();

    public ImportacaoUsuarioResponse() {
    }

    public ImportacaoUsuarioResponse(
            int usuariosImportados,
            int usuariosComErro,
            List<ErroImportacao> erros) {

        this.usuariosImportados =
                usuariosImportados;

        this.usuariosComErro =
                usuariosComErro;

        this.erros =
                erros;
    }

    public int getUsuariosImportados() {
        return usuariosImportados;
    }

    public void setUsuariosImportados(
            int usuariosImportados) {

        this.usuariosImportados =
                usuariosImportados;
    }

    public int getUsuariosComErro() {
        return usuariosComErro;
    }

    public void setUsuariosComErro(
            int usuariosComErro) {

        this.usuariosComErro =
                usuariosComErro;
    }

    public List<ErroImportacao> getErros() {
        return erros;
    }

    public void setErros(
            List<ErroImportacao> erros) {

        this.erros = erros;
    }

    public static class ErroImportacao {

        private int linha;

        private String motivo;

        public ErroImportacao() {
        }

        public ErroImportacao(
                int linha,
                String motivo) {

            this.linha = linha;
            this.motivo = motivo;
        }

        public int getLinha() {
            return linha;
        }

        public void setLinha(int linha) {
            this.linha = linha;
        }

        public String getMotivo() {
            return motivo;
        }

        public void setMotivo(String motivo) {
            this.motivo = motivo;
        }
    }
}
