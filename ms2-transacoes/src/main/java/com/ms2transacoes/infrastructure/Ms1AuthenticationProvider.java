package com.ms2transacoes.infrastructure;

import com.ms2transacoes.dto.LoginRequest;
import com.ms2transacoes.dto.LoginResponse;
import com.ms2transacoes.ports.output.UsuarioClientPort;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class Ms1AuthenticationProvider
        implements AuthenticationProvider {

    private final UsuarioClientPort usuarioClientPort;

    public Ms1AuthenticationProvider(
            UsuarioClientPort usuarioClientPort) {

        this.usuarioClientPort =
                usuarioClientPort;
    }

    @Override
    public Authentication authenticate(
            Authentication authentication)
            throws AuthenticationException {

        String email =
                authentication.getName();

        String senha =
                authentication.getCredentials()
                        .toString();

        try {

            LoginResponse usuario =
                    usuarioClientPort.autenticar(
                            new LoginRequest(
                                    email,
                                    senha
                            )
                    );

            UsuarioAutenticado principal =
                    new UsuarioAutenticado(
                            usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail()
                    );

            return new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    Collections.emptyList()
            );

        } catch (Exception e) {

            throw new BadCredentialsException(
                    "Credenciais inválidas"
            );
        }
    }

    @Override
    public boolean supports(
            Class<?> authentication) {

        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(
                        authentication
                );
    }
}