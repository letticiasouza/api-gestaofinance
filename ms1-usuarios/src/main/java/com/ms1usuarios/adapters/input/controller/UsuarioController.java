package com.ms1usuarios.adapters.input.controller;

import com.ms1usuarios.dto.CreateUsuarioRequest;
import com.ms1usuarios.dto.ImportacaoUsuarioResponse;
import com.ms1usuarios.dto.LoginRequest;
import com.ms1usuarios.dto.LoginResponse;
import com.ms1usuarios.dto.UpdateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioAuthResponse;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.CreateUsuarioUseCase;
import com.ms1usuarios.ports.input.DeleteUsuarioUseCase;
import com.ms1usuarios.ports.input.FindUsuarioAuthUseCase;
import com.ms1usuarios.ports.input.FindUsuarioUseCase;
import com.ms1usuarios.ports.input.ImportUsuariosCsvUseCase;
import com.ms1usuarios.ports.input.ListUsuariosUseCase;
import com.ms1usuarios.ports.input.LoginUseCase;
import com.ms1usuarios.ports.input.UpdateUsuarioUseCase;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Usuários",
        description = "Operações de gerenciamento de usuários"
)
@RestController
@RequestMapping("/users")
public class UsuarioController {

    private final CreateUsuarioUseCase createUsuarioUseCase;
    private final FindUsuarioUseCase findUsuarioUseCase;
    private final FindUsuarioAuthUseCase findUsuarioAuthUseCase;
    private final ListUsuariosUseCase listUsuariosUseCase;
    private final UpdateUsuarioUseCase updateUsuarioUseCase;
    private final DeleteUsuarioUseCase deleteUsuarioUseCase;
    private final ImportUsuariosCsvUseCase importUsuariosCsvUseCase;
    private final LoginUseCase loginUseCase;

    public UsuarioController(
            CreateUsuarioUseCase createUsuarioUseCase,
            FindUsuarioUseCase findUsuarioUseCase,
            FindUsuarioAuthUseCase findUsuarioAuthUseCase,
            ListUsuariosUseCase listUsuariosUseCase,
            UpdateUsuarioUseCase updateUsuarioUseCase,
            DeleteUsuarioUseCase deleteUsuarioUseCase,
            ImportUsuariosCsvUseCase importUsuariosCsvUseCase,
            LoginUseCase loginUseCase) {

        this.createUsuarioUseCase = createUsuarioUseCase;
        this.findUsuarioUseCase = findUsuarioUseCase;
        this.findUsuarioAuthUseCase = findUsuarioAuthUseCase;
        this.listUsuariosUseCase = listUsuariosUseCase;
        this.updateUsuarioUseCase = updateUsuarioUseCase;
        this.deleteUsuarioUseCase = deleteUsuarioUseCase;
        this.importUsuariosCsvUseCase = importUsuariosCsvUseCase;
        this.loginUseCase = loginUseCase;
    }

    @Operation(
            summary = "Cria um novo usuário"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(
            @Valid @RequestBody CreateUsuarioRequest request) {

        return createUsuarioUseCase.criar(request);
    }

    @Hidden
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return loginUseCase.login(request);
    }

    @Hidden
    @GetMapping("/auth/{email}")
    public UsuarioAuthResponse buscarUsuarioAuth(
            @PathVariable String email) {

        return findUsuarioAuthUseCase.buscarPorEmail(
                email
        );
    }

    @Operation(
            summary = "Importa usuários a partir de um arquivo CSV"
    )
    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ImportacaoUsuarioResponse importarCsv(

            @Parameter(
                    description = "Arquivo CSV",
                    schema = @Schema(
                            type = "string",
                            format = "binary"
                    )
            )
            @RequestPart("arquivo") MultipartFile arquivo) {

        return importUsuariosCsvUseCase.importar(arquivo);
    }

    @Operation(
            summary = "Lista todos os usuários"
    )
    @GetMapping
    public List<UsuarioResponse> listarTodos() {

        return listUsuariosUseCase.listarTodos();
    }

    @Operation(
            summary = "Busca um usuário por identificador"
    )
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(
            @PathVariable UUID id) {

        return findUsuarioUseCase.buscarPorId(id);
    }

    @Operation(
            summary = "Atualiza os dados do usuário autenticado"
    )
    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUsuarioRequest request) {

        return updateUsuarioUseCase.atualizar(id, request);
    }

    @Operation(
            summary = "Remove um usuário"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable UUID id) {

        deleteUsuarioUseCase.deletar(id);
    }
}