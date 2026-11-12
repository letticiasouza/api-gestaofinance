package com.ms2transacoes.adapters.output.client;

import com.ms2transacoes.dto.LoginRequest;
import com.ms2transacoes.dto.LoginResponse;
import com.ms2transacoes.ports.output.UsuarioClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClientAdapter
        implements UsuarioClientPort {

    private final RestTemplate restTemplate;

    @Value("${ms1.base-url}")
    private String ms1BaseUrl;

    public UsuarioClientAdapter() {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);

        this.restTemplate =
                new RestTemplate(factory);
    }

    @Override
    public LoginResponse autenticar(
            LoginRequest request) {

        String url =
                ms1BaseUrl
                        + "/users/login";

        return restTemplate.postForObject(
                url,
                request,
                LoginResponse.class
        );
    }
}