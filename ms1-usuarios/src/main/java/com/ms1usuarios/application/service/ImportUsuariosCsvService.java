package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.exception.CabecalhoCsvInvalidoException;
import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.ImportacaoUsuarioResponse;
import com.ms1usuarios.ports.input.ImportUsuariosCsvUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.StringReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ImportUsuariosCsvService
        implements ImportUsuariosCsvUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    private final PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            );

    public ImportUsuariosCsvService(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepositoryPort =
                usuarioRepositoryPort;

        this.passwordEncoder =
                passwordEncoder;
    }

    @Override
    public ImportacaoUsuarioResponse importar(
            MultipartFile arquivo) {

        int usuariosImportados = 0;

        int usuariosComErro = 0;

        List<ImportacaoUsuarioResponse.ErroImportacao>
                erros = new ArrayList<>();

        try {

            String conteudo =
                    new String(
                            arquivo.getBytes(),
                            StandardCharsets.UTF_8
                    );

            char delimitador =
                    conteudo.contains(";")
                            ? ';'
                            : ',';

            try (

                    CSVParser parser =
                            CSVFormat.DEFAULT
                                    .builder()
                                    .setDelimiter(delimitador)
                                    .setHeader()
                                    .setSkipHeaderRecord(false)
                                    .build()
                                    .parse(
                                            new StringReader(
                                                    conteudo
                                            )
                                    )

            ) {

                List<String> headers =
                        parser.getHeaderNames();

                String primeiroHeader =
                        headers.get(0)
                                .replace("\uFEFF", "");

                if (headers.size() != 3
                        || !primeiroHeader
                        .equalsIgnoreCase("nome")
                        || !headers.get(1)
                        .equalsIgnoreCase("email")
                        || !headers.get(2)
                        .equalsIgnoreCase("senha")) {

                    throw new CabecalhoCsvInvalidoException();
                }

                for (CSVRecord record : parser) {

                    int numeroLinha =
                            (int) record.getRecordNumber() + 1;

                    try {

                        String nome =
                                record.get("nome").trim();

                        String email =
                                record.get("email")
                                        .trim()
                                        .toLowerCase();

                        String senha =
                                record.get("senha").trim();

                        if (!EMAIL_PATTERN
                                .matcher(email)
                                .matches()) {

                            erros.add(
                                    new ImportacaoUsuarioResponse
                                            .ErroImportacao(
                                            numeroLinha,
                                            "Email inválido"
                                    )
                            );

                            usuariosComErro++;

                            continue;
                        }

                        boolean emailExiste =
                                usuarioRepositoryPort
                                        .buscarPorEmail(email)
                                        .isPresent();

                        if (emailExiste) {

                            erros.add(
                                    new ImportacaoUsuarioResponse
                                            .ErroImportacao(
                                            numeroLinha,
                                            "Email já cadastrado"
                                    )
                            );

                            usuariosComErro++;

                            continue;
                        }

                        Usuario usuario =
                                new Usuario(
                                        UUID.randomUUID(),
                                        nome,
                                        email,
                                        passwordEncoder.encode(
                                                senha
                                        )
                                );

                        usuarioRepositoryPort
                                .salvar(usuario);

                        usuariosImportados++;

                    } catch (Exception e) {

                        erros.add(
                                new ImportacaoUsuarioResponse
                                        .ErroImportacao(
                                        numeroLinha,
                                        e.getMessage()
                                )
                        );

                        usuariosComErro++;
                    }
                }

            }

        } catch (CabecalhoCsvInvalidoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao processar arquivo CSV",
                    e
            );
        }

        return new ImportacaoUsuarioResponse(
                usuariosImportados,
                usuariosComErro,
                erros
        );
    }
}