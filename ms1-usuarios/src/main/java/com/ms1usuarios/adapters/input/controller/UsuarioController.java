package com.ms1usuarios.adapters.input.controller;

import com.ms1usuarios.dto.CreateUsuarioRequest;
import com.ms1usuarios.dto.ImportacaoUsuarioResponse;
import com.ms1usuarios.dto.UpdateUsuarioRequest;
import com.ms1usuarios.dto.UsuarioResponse;
import com.ms1usuarios.ports.input.CreateUsuarioUseCase;
import com.ms1usuarios.ports.input.DeleteUsuarioUseCase;
import com.ms1usuarios.ports.input.FindUsuarioUseCase;
import com.ms1usuarios.ports.input.ImportUsuariosCsvUseCase;
import com.ms1usuarios.ports.input.ListUsuariosUseCase;
import com.ms1usuarios.ports.input.UpdateUsuarioUseCase;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    private final CreateUsuarioUseCase createUsuarioUseCase;
    private final FindUsuarioUseCase findUsuarioUseCase;
    private final ListUsuariosUseCase listUsuariosUseCase;
    private final UpdateUsuarioUseCase updateUsuarioUseCase;
    private final DeleteUsuarioUseCase deleteUsuarioUseCase;
    private final ImportUsuariosCsvUseCase importUsuariosCsvUseCase;

    public UsuarioController(
            CreateUsuarioUseCase createUsuarioUseCase,
            FindUsuarioUseCase findUsuarioUseCase,
            ListUsuariosUseCase listUsuariosUseCase,
            UpdateUsuarioUseCase updateUsuarioUseCase,
            DeleteUsuarioUseCase deleteUsuarioUseCase,
            ImportUsuariosCsvUseCase importUsuariosCsvUseCase) {

        this.createUsuarioUseCase = createUsuarioUseCase;
        this.findUsuarioUseCase = findUsuarioUseCase;
        this.listUsuariosUseCase = listUsuariosUseCase;
        this.updateUsuarioUseCase = updateUsuarioUseCase;
        this.deleteUsuarioUseCase = deleteUsuarioUseCase;
        this.importUsuariosCsvUseCase = importUsuariosCsvUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(
            @Valid @RequestBody CreateUsuarioRequest request) {

        return createUsuarioUseCase.criar(request);
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(
            @PathVariable UUID id) {

        return findUsuarioUseCase.buscarPorId(id);
    }

    @GetMapping
    public List<UsuarioResponse> listarTodos() {

        return listUsuariosUseCase.listarTodos();
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUsuarioRequest request) {

        return updateUsuarioUseCase.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable UUID id) {

        deleteUsuarioUseCase.deletar(id);
    }

    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImportacaoUsuarioResponse importarCsv(

            @Parameter(
                    description = "Arquivo CSV",
                    schema = @Schema(
                            type = "string",
                            format = "binary"))
            @RequestPart("arquivo") MultipartFile arquivo) {

        return importUsuariosCsvUseCase.importar(arquivo);
    }
}