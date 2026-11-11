package com.ms2transacoes.infrastructure;

import com.ms2transacoes.dto.UsuarioAuthResponse;
import com.ms2transacoes.ports.output.UsuarioClientPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioClientPort
            usuarioClientPort;

    public CustomUserDetailsService(
            UsuarioClientPort usuarioClientPort) {

        this.usuarioClientPort =
                usuarioClientPort;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        System.out.println(
                "AUTENTICANDO EMAIL: " + email
        );

        try {

            UsuarioAuthResponse usuario =
                    usuarioClientPort
                            .buscarPorEmail(email);

            return new UsuarioAutenticado(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getSenha()
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new UsernameNotFoundException(
                    "Usuário não encontrado",
                    e
            );
        }
    }
}