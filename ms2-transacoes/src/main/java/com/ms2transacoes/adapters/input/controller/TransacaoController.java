package com.ms2transacoes.adapters.input.controller;

import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.DespesasAgrupadasResponse;
import com.ms2transacoes.dto.ModoAnalise;
import com.ms2transacoes.dto.ResumoDespesasResponse;
import com.ms2transacoes.dto.SaldoResponse;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.input.CreateTransacaoUseCase;
import com.ms2transacoes.ports.input.ExportarRelatorioCsvUseCase;
import com.ms2transacoes.ports.input.FindTransacaoUseCase;
import com.ms2transacoes.ports.input.GetSaldoUseCase;
import com.ms2transacoes.ports.input.ListDespesasAgrupadasUseCase;
import com.ms2transacoes.ports.input.ListTransacoesUseCase;
import com.ms2transacoes.ports.input.ResumoDespesasUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CreateTransacaoUseCase createTransacaoUseCase;

    private final FindTransacaoUseCase findTransacaoUseCase;

    private final ListTransacoesUseCase listTransacoesUseCase;

    private final GetSaldoUseCase getSaldoUseCase;

    private final ResumoDespesasUseCase resumoDespesasUseCase;

    private final ListDespesasAgrupadasUseCase
            listDespesasAgrupadasUseCase;

    private final ExportarRelatorioCsvUseCase
            exportarRelatorioCsvUseCase;

    public TransacaoController(
            CreateTransacaoUseCase createTransacaoUseCase,
            FindTransacaoUseCase findTransacaoUseCase,
            ListTransacoesUseCase listTransacoesUseCase,
            GetSaldoUseCase getSaldoUseCase,
            ResumoDespesasUseCase resumoDespesasUseCase,
            ListDespesasAgrupadasUseCase listDespesasAgrupadasUseCase,
            ExportarRelatorioCsvUseCase exportarRelatorioCsvUseCase) {

        this.createTransacaoUseCase =
                createTransacaoUseCase;

        this.findTransacaoUseCase =
                findTransacaoUseCase;

        this.listTransacoesUseCase =
                listTransacoesUseCase;

        this.getSaldoUseCase =
                getSaldoUseCase;

        this.resumoDespesasUseCase =
                resumoDespesasUseCase;

        this.listDespesasAgrupadasUseCase =
                listDespesasAgrupadasUseCase;

        this.exportarRelatorioCsvUseCase =
                exportarRelatorioCsvUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoResponse criar(
            @Valid @RequestBody
            CreateTransacaoRequest request) {

        return createTransacaoUseCase.criar(
                request
        );
    }

    @GetMapping("/{id}")
    public TransacaoResponse buscarPorId(
            @PathVariable UUID id) {

        return findTransacaoUseCase.buscarPorId(
                id
        );
    }

    @GetMapping
    public List<TransacaoResponse> listar(

            @RequestParam(required = false)
            LocalDateTime dataInicio,

            @RequestParam(required = false)
            LocalDateTime dataFim,

            @RequestParam(required = false)
            TipoTransacao tipo) {

        return listTransacoesUseCase.listar(
                dataInicio,
                dataFim,
                tipo
        );
    }

    @GetMapping("/saldo")
    public SaldoResponse consultarSaldo() {

        return getSaldoUseCase.consultarSaldo();
    }

    @GetMapping("/despesas/resumo")
    public ResumoDespesasResponse resumirDespesas(

            @RequestParam(required = false)
            LocalDate dataInicio,

            @RequestParam(required = false)
            LocalDate dataFim) {

        return resumoDespesasUseCase.resumir(
                dataInicio,
                dataFim
        );
    }

    @GetMapping("/despesas")
    public DespesasAgrupadasResponse listarDespesas(

            @RequestParam(required = false)
            LocalDate dataInicio,

            @RequestParam(required = false)
            LocalDate dataFim,

            @RequestParam ModoAnalise modo) {

        return listDespesasAgrupadasUseCase.listar(
                dataInicio,
                dataFim,
                modo
        );
    }

    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> exportarRelatorio(

            @RequestParam(required = false)
            LocalDate dataInicio,

            @RequestParam(required = false)
            LocalDate dataFim) {

        return exportarRelatorioCsvUseCase.exportar(
                dataInicio,
                dataFim
        );
    }
}