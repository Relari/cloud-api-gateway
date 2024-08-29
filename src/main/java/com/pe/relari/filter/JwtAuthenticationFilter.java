package com.pe.relari.filter;

import com.pe.relari.dao.api.SecurityAuthenticationApi;
import com.pe.relari.model.api.TokenRequest;
import com.pe.relari.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private SecurityAuthenticationApi authenticationApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        }

        String authHeader = Objects.requireNonNull(
                exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)
        ).get(0);

        if (authHeader!= null && authHeader.startsWith(Constants.TOKEN_HEADER)) {
            authHeader = authHeader.substring(Constants.TOKEN_HEADER.length());

            var request = new TokenRequest(authHeader);

            authenticationApi.validate(request)
                    .subscribe((validateResponse, throwable) -> {
                        if (Boolean.FALSE.equals(validateResponse.getIsTokenExpired())) {
                            log.debug("Token activo");
                        } else {
                            log.error("Error al validar el token", throwable);
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        }
                    })
                    .dispose();
        }

        return chain.filter(exchange);
    }
}
