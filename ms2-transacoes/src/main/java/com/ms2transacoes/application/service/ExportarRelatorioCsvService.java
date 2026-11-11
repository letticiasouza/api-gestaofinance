package com.ms2transacoes.application.service;

import com.ms2transacoes.domain.model.Transacao;
import com.ms2transacoes.infrastructure.UsuarioAutenticadoProvider;
import com.ms2transacoes.ports.input.ExportarRelatorioCsvUseCase;
import com.ms2transacoes.ports.output.TransacaoRepositoryPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportarRelatorioCsvService
        implements ExportarRelatorioCsvUseCase {

    private final TransacaoRepositoryPort
            transacaoRepositoryPort;

    private final UsuarioAutenticadoProvider
            usuarioAutenticadoProvider;

    public ExportarRelatorioCsvService(
            TransacaoRepositoryPort
                    transacaoRepositoryPort,
            UsuarioAutenticadoProvider
                    usuarioAutenticadoProvider) {

        this.transacaoRepositoryPort =
                transacaoRepositoryPort;

        this.usuarioAutenticadoProvider =
                usuarioAutenticadoProvider;
    }

    @Override
    public ResponseEntity<byte[]> exportar(
            LocalDate dataInicio,
            LocalDate dataFim) {

        var usuarioId =
                usuarioAutenticadoProvider.getId();

        if (dataInicio == null || dataFim == null) {

            YearMonth mesAtual =
                    YearMonth.now();

            dataInicio =
                    mesAtual.atDay(1);

            dataFim =
                    mesAtual.atEndOfMonth();
        }

        List<Transacao> transacoes =
                transacaoRepositoryPort
                        .listarPorPeriodo(
                                usuarioId,
                                dataInicio.atStartOfDay(),
                                dataFim.atTime(
                                        23,
                                        59,
                                        59
                                )
                        );

        StringBuilder csv =
                new StringBuilder();

        csv.append('\uFEFF');

        csv.append(
                "Data;Tipo;Categoria;Descrição;Valor (R$);Status;Motivo\n"
        );

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm:ss"
                );

        for (Transacao transacao : transacoes) {

            csv.append(
                    escapar(
                            transacao.getDataHora()
                                    .format(formatter)
                    )
            );

            csv.append(';');

            csv.append(
                    escapar(
                            transacao.getTipo().name()
                    )
            );

            csv.append(';');

            csv.append(
                    escapar(
                            transacao.getCategoria() != null
                                    ? transacao.getCategoria().name()
                                    : ""
                    )
            );

            csv.append(';');

            csv.append(
                    escapar(
                            transacao.getDescricao()
                    )
            );

            csv.append(';');

            csv.append(
                    transacao.getValor()
                            .toString()
                            .replace('.', ',')
            );

            csv.append(';');

            csv.append(
                    escapar(
                            transacao.getStatus().name()
                    )
            );

            csv.append(';');

            csv.append(
                    escapar(
                            transacao.getMotivoRejeicao()
                    )
            );

            csv.append('\n');
        }

        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=relatorio-transacoes.csv"
                )

                .header(
                        HttpHeaders.CONTENT_TYPE,
                        "application/vnd.ms-excel; charset=UTF-8"
                )

                .body(
                        csv.toString()
                                .getBytes(
                                        StandardCharsets.UTF_8
                                )
                );
    }

    private String escapar(String valor) {

        if (valor == null) {
            return "";
        }

        if (valor.contains(";")
                || valor.contains("\"")
                || valor.contains("\n")) {

            return "\"" +
                    valor.replace("\"", "\"\"")
                    + "\"";
        }

        return valor;
    }
}