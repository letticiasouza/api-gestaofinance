package com.ms2transacoes.adapters.output.client;

import com.ms2transacoes.domain.exception.ServicoSaldoIndisponivelException;
import com.ms2transacoes.dto.ContaMockApiResponse;
import com.ms2transacoes.ports.output.SaldoLimiteClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SaldoLimiteMockApiClient
        implements SaldoLimiteClientPort {

    private final RestClient restClient;

    public SaldoLimiteMockApiClient(
            @Value("${mockapi.base-url}")
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
    public ContaMockApiResponse buscarContaPorUsuario(
            String usuarioId) {

        try {

            List<ContaMockApiResponse> contas =
                    restClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/contas")
                                    .queryParam(
                                            "usuarioId",
                                            usuarioId
                                    )
                                    .build())
                            .retrieve()
                            .body(new ParameterizedTypeReference<>() {
                            });

            if (contas == null || contas.isEmpty()) {
                return null;
            }

            return contas.getFirst();

        } catch (HttpClientErrorException e) {

            if (e.getStatusCode()
                    == HttpStatus.NOT_FOUND) {

                return null;
            }

            throw new ServicoSaldoIndisponivelException();

        } catch (RestClientException e) {

            throw new ServicoSaldoIndisponivelException();
        }
    }

    @Override
    public void atualizarSaldo(
            String usuarioId,
            BigDecimal novoSaldo) {

        try {

            ContaMockApiResponse conta =
                    buscarContaPorUsuario(
                            usuarioId
                    );

            if (conta == null) {
                return;
            }

            conta.setSaldo(
                    novoSaldo
            );

            restClient.put()
                    .uri(
                            "/contas/{id}",
                            conta.getId()
                    )
                    .body(conta)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientException e) {

            throw new ServicoSaldoIndisponivelException();
        }
    }
}