package com.ms2transacoes.infrastructure;

import com.ms2transacoes.domain.exception.ServicoSaldoIndisponivelException;
import com.ms2transacoes.domain.exception.TransacaoNaoEncontradaException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import com.ms2transacoes.domain.exception.AcessoNegadoException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            TransacaoNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>>
    tratarTransacaoNaoEncontrada(
            TransacaoNaoEncontradaException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "mensagem", ex.getMessage()
                ));
    }

    @ExceptionHandler(
            ServicoSaldoIndisponivelException.class)
    public ResponseEntity<Map<String, Object>>
    tratarServicoSaldoIndisponivel(
            ServicoSaldoIndisponivelException ex) {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "status", 503,
                        "mensagem", ex.getMessage()
                ));
    }

    @ExceptionHandler(
            AcessoNegadoException.class
    )
    public ResponseEntity<Map<String, Object>>
    tratarAcessoNegado(
            AcessoNegadoException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "status", 403,
                        "mensagem", ex.getMessage()
                ));
    }
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
    tratarValidacao(
            MethodArgumentNotValidException ex) {

        Map<String, String> erros =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(
                        erro ->
                                erros.put(
                                        erro.getField(),
                                        erro.getDefaultMessage()
                                )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "status", 400,
                                "erros", erros
                        )
                );
    }
}