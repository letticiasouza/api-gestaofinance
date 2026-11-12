package com.ms2transacoes.adapters.output.client;

import com.ms2transacoes.ports.output.CambioClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class BrasilApiClient
        implements CambioClientPort {

    private final RestClient restClient;

    public BrasilApiClient(
            @Value("${brasilapi.base-url}")
            String baseUrl) {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);

        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Cotacao buscarUsd() {

        return buscarCotacao("USD");
    }

    @Override
    public Cotacao buscarEur() {

        return buscarCotacao("EUR");
    }

    private Cotacao buscarCotacao(
            String moeda) {

        try {

            LocalDate dataConsulta =
                    LocalDate.now()
                            .minusDays(1);

            Map<?, ?> response =
                    restClient.get()
                            .uri(
                                    "/api/cambio/v1/cotacao/{moeda}/{data}",
                                    moeda,
                                    dataConsulta
                            )
                            .retrieve()
                            .body(Map.class);

            if (response == null) {

                return new Cotacao(
                        moeda,
                        null,
                        null
                );
            }

            List<?> cotacoes =
                    (List<?>) response.get("cotacoes");

            if (cotacoes == null
                    || cotacoes.isEmpty()) {

                return new Cotacao(
                        moeda,
                        null,
                        null
                );
            }

            Map<?, ?> ultimaCotacao =
                    (Map<?, ?>) cotacoes.get(
                            cotacoes.size() - 1
                    );

            BigDecimal taxa =
                    new BigDecimal(
                            ultimaCotacao
                                    .get("cotacao_venda")
                                    .toString()
                    );

            String data =
                    response.get("data")
                            .toString();

            return new Cotacao(
                    moeda,
                    taxa,
                    LocalDate.parse(data)
            );

        } catch (Exception ex) {

            return new Cotacao(
                    moeda,
                    null,
                    null
            );
        }
    }
}