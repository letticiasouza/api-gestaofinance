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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(
        name = "Transações",
        description = "Operações relacionadas às movimentações financeiras"
)
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



    @Operation(
            summary = "Realizar uma transação",
            description = """
                Permite realizar depósitos,
                retiradas, compras e transferências.
                """
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoResponse criar(
            @Valid @RequestBody
            CreateTransacaoRequest request) {

        return createTransacaoUseCase.criar(
                request
        );
    }


    @Operation(
            summary = "Buscar transação por identificador",
            description = """
                Retorna os detalhes de uma transação específica
                do usuário autenticado.
                """
    )
    @GetMapping("/{id}")
    public TransacaoResponse buscarPorId(
            @PathVariable UUID id) {

        return findTransacaoUseCase.buscarPorId(
                id
        );
    }

    @Operation(
            summary = "Listar transações",
            description = """
                Lista as transações do usuário autenticado,
                permitindo filtro por período e tipo.
                """
    )
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


    @Operation(
            summary = "Consultar saldo",
            description = """
                Consulta o saldo atual do usuário autenticado.
                """
    )
    @GetMapping("/saldo")
    public SaldoResponse consultarSaldo() {

        return getSaldoUseCase.consultarSaldo();
    }

    @Operation(
            summary = "Resumo de despesas",
            description = """
                Exibe o total geral gasto e os totais
                agrupados por categoria.
                """
    )
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

    @Operation(
            summary = "Listar despesas agrupadas",
            description = """
                Retorna os gastos agrupados
                por dia ou mês.
                """
    )
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

    @Operation(
            summary = "Exportar relatório CSV",
            description = """
                Gera um arquivo CSV contendo as transações
                do usuário autenticado para o período informado.
                """
    )
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