package com.ms2transacoes.adapters.input.controller;

import com.ms2transacoes.dto.SaldoResponse;
import com.ms2transacoes.domain.model.TipoTransacao;
import com.ms2transacoes.dto.CreateTransacaoRequest;
import com.ms2transacoes.dto.TransacaoResponse;
import com.ms2transacoes.ports.input.CreateTransacaoUseCase;
import com.ms2transacoes.ports.input.FindTransacaoUseCase;
import com.ms2transacoes.ports.input.GetSaldoUseCase;
import com.ms2transacoes.ports.input.ListTransacoesUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    public TransacaoController(
            CreateTransacaoUseCase createTransacaoUseCase,
            FindTransacaoUseCase findTransacaoUseCase,
            ListTransacoesUseCase listTransacoesUseCase,
            GetSaldoUseCase getSaldoUseCase) {

        this.createTransacaoUseCase =
                createTransacaoUseCase;

        this.findTransacaoUseCase =
                findTransacaoUseCase;

        this.listTransacoesUseCase =
                listTransacoesUseCase;

        this.getSaldoUseCase =
                getSaldoUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoResponse criar(
            @Valid @RequestBody CreateTransacaoRequest request) {

        return createTransacaoUseCase.criar(request);
    }

    @GetMapping("/{id}")
    public TransacaoResponse buscarPorId(
            @PathVariable UUID id) {

        return findTransacaoUseCase.buscarPorId(id);
    }

    @GetMapping
    public List<TransacaoResponse> listar(

            @RequestParam UUID usuarioId,

            @RequestParam(required = false)
            LocalDateTime dataInicio,

            @RequestParam(required = false)
            LocalDateTime dataFim,

            @RequestParam(required = false)
            TipoTransacao tipo) {

        return listTransacoesUseCase.listar(
                usuarioId,
                dataInicio,
                dataFim,
                tipo
        );
    }

    @GetMapping("/saldo")
    public SaldoResponse consultarSaldo(
            @RequestParam UUID usuarioId) {

        return getSaldoUseCase.consultarSaldo(
                usuarioId
        );
    }
}