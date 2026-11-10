package com.ms1usuarios.infrastructure;

import com.ms1usuarios.domain.exception.AcessoNegadoException;
import com.ms1usuarios.domain.exception.CabecalhoCsvInvalidoException;
import com.ms1usuarios.domain.exception.EmailJaCadastradoException;
import com.ms1usuarios.domain.exception.LoginInvalidoException;
import com.ms1usuarios.domain.exception.UsuarioNaoEncontradoException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> tratarEmailJaCadastrado(
            EmailJaCadastradoException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "mensagem", ex.getMessage()
                ));
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "mensagem", ex.getMessage()
                ));
    }

    @ExceptionHandler(LoginInvalidoException.class)
    public ResponseEntity<Map<String, Object>> tratarLoginInvalido(
            LoginInvalidoException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "status", 401,
                        "mensagem", ex.getMessage()
                ));
    }

    @ExceptionHandler(CabecalhoCsvInvalidoException.class)
    public ResponseEntity<Map<String, Object>>
    tratarCabecalhoCsvInvalido(
            CabecalhoCsvInvalidoException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "mensagem", ex.getMessage()
                ));
    }

    @ExceptionHandler(AcessoNegadoException.class)
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

    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> erros =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        erros.put(
                                erro.getField(),
                                erro.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "status", 400,
                        "erros", erros
                ));
    }

    @Override
    protected ResponseEntity<Object>
    handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "status", 400,
                        "mensagem", "JSON inválido"
                ));
    }

    @Override
    protected ResponseEntity<Object>
    handleMissingServletRequestPart(
            MissingServletRequestPartException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "status", 400,
                        "mensagem", "Arquivo não enviado"
                ));
    }

    @ExceptionHandler(
            MethodArgumentTypeMismatchException.class
    )
    public ResponseEntity<Map<String, Object>>
    tratarTipoInvalido(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "status", 400,
                        "mensagem", "Parâmetro inválido"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    tratarErroGenerico(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "status", 500,
                        "mensagem",
                        "Erro interno da aplicação"
                ));
    }
}