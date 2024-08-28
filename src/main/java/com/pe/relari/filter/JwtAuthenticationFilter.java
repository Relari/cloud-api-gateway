package com.pe.relari.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${application.http-client.security-jwt.path}")
    private String securityJwtPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        }

        String authHeader = Objects.requireNonNull(
                exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)
        ).get(0);

        if (authHeader!= null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);

            String builder = securityJwtPath + "?token=" + authHeader;

            restTemplate.getForObject(builder, Objects.class);

        }

        return chain.filter(exchange);
    }
}
