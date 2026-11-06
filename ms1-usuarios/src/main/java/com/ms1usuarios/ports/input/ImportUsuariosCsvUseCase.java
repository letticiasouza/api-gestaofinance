package com.ms1usuarios.ports.input;

import com.ms1usuarios.dto.ImportacaoUsuarioResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImportUsuariosCsvUseCase {

    ImportacaoUsuarioResponse importar(MultipartFile arquivo);

}