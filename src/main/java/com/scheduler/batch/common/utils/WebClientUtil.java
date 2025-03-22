package com.scheduler.batch.common.utils;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebClientUtil {

    private static final int CONNECT_TIMEOUT_MILLISECOND = 10 * 1000;
    private static final long RESPONSE_TIMEOUT_MILLISECOND = 60 * 1000L;
    private static final long READ_TIMEOUT_MILLISECOND = 3 * 60 * 1000L;
    private static final long WRITE_TIMEOUT_MILLISECOND = 60 * 1000L;

    private static final WebClient webClient;

    static {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLISECOND)
                .responseTimeout(Duration.ofMillis(RESPONSE_TIMEOUT_MILLISECOND))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_MILLISECOND, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_MILLISECOND, TimeUnit.MILLISECONDS))
                );

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        webClient = WebClient.builder()
                .uriBuilderFactory(factory)	// 추가
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    public static String callApiXMLString(String apiUrl, MultiValueMap<String, String> param) {
        String url = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParams(param)
                .build()
                .toUriString();

        log.info("Calling API: {}", url);

        Mono<String> response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);

        return response.block(); // 동기식 호출
    }
}
