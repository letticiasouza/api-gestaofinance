package com.ms1usuarios.application.service;

import com.ms1usuarios.domain.model.Usuario;
import com.ms1usuarios.dto.ImportacaoUsuarioResponse;
import com.ms1usuarios.ports.input.ImportUsuariosCsvUseCase;
import com.ms1usuarios.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

@Service
public class ImportUsuariosCsvService
        implements ImportUsuariosCsvUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ImportUsuariosCsvService(
            UsuarioRepositoryPort usuarioRepositoryPort) {

        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public ImportacaoUsuarioResponse importar(
            MultipartFile arquivo) {

        int usuariosImportados = 0;
        int erros = 0;

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        arquivo.getInputStream()))
        ) {

            String linha;

            boolean cabecalho = true;

            while ((linha = reader.readLine()) != null) {

                if (cabecalho) {
                    cabecalho = false;
                    continue;
                }

                try {

                    String[] campos = linha.split(",");

                    String nome = campos[0].trim();
                    String email = campos[1].trim();
                    String senha = campos[2].trim();

                    boolean emailExiste =
                            usuarioRepositoryPort
                                    .buscarPorEmail(email)
                                    .isPresent();

                    if (emailExiste) {
                        erros++;
                        continue;
                    }

                    Usuario usuario = new Usuario(
                            UUID.randomUUID(),
                            nome,
                            email,
                            senha
                    );

                    usuarioRepositoryPort.salvar(usuario);

                    usuariosImportados++;

                } catch (Exception e) {
                    erros++;
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao processar arquivo CSV",
                    e
            );
        }

        return new ImportacaoUsuarioResponse(
                usuariosImportados,
                erros
        );
    }
}