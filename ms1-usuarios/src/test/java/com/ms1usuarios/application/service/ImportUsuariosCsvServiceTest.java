package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.ImportacaoUsuarioResponse;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportUsuariosCsvServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ImportUsuariosCsvService importUsuariosCsvService;

    @Test
    void deveImportarUsuarioComSucesso() {

        String csv = """
                nome,email,senha
                Vitoria,vitoria@email.com,123456
                """;

        MockMultipartFile arquivo =
                new MockMultipartFile(
                        "file",
                        "usuarios.csv",
                        "text/csv",
                        csv.getBytes()
                );

        when(usuarioRepositoryPort.buscarPorEmail(
                "vitoria@email.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("senhaHash");

        ImportacaoUsuarioResponse response =
                importUsuariosCsvService.importar(arquivo);

        assertEquals(
                1,
                response.getUsuariosImportados()
        );

        assertEquals(
                0,
                response.getErros().size()
        );

        verify(usuarioRepositoryPort)
                .salvar(any(Usuario.class));
    }

    @Test
    void deveIgnorarEmailDuplicado() {

        String csv = """
                nome,email,senha
                Vitoria,vitoria@email.com,123456
                """;

        MockMultipartFile arquivo =
                new MockMultipartFile(
                        "file",
                        "usuarios.csv",
                        "text/csv",
                        csv.getBytes()
                );

        Usuario usuarioExistente =
                new Usuario(
                        null,
                        "Vitoria",
                        "vitoria@email.com",
                        "123456"
                );

        when(usuarioRepositoryPort.buscarPorEmail(
                "vitoria@email.com"))
                .thenReturn(Optional.of(usuarioExistente));

        ImportacaoUsuarioResponse response =
                importUsuariosCsvService.importar(arquivo);

        assertEquals(
                0,
                response.getUsuariosImportados()
        );

        assertEquals(
                1,
                response.getErros().size()
        );
    }
}