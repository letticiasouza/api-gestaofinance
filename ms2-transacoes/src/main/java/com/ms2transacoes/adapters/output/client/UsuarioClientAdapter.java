package com.ms2transacoes.adapters.output.client;

import com.ms2transacoes.dto.UsuarioAuthResponse;
import com.ms2transacoes.ports.output.UsuarioClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClientAdapter
        implements UsuarioClientPort {

    private final RestTemplate restTemplate =
            new RestTemplate();

    @Value("${ms1.base-url}")
    private String ms1BaseUrl;

    @Override
    public UsuarioAuthResponse buscarPorEmail(
            String email) {

        String url =
                ms1BaseUrl
                        + "/users/auth/"
                        + email;

        System.out.println(
                "BUSCANDO USUARIO NO MS1: " + email
        );

        System.out.println(
                "URL: " + url
        );

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UsuarioAuthResponse>() {
                }
        ).getBody();
    }
}