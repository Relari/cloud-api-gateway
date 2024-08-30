package com.pe.relari.filter;

import com.pe.relari.service.SecurityService;
import com.pe.relari.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    private SecurityService securityService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        var headersRequest = exchange.getRequest().getHeaders();
        var headerAuthorization = headersRequest.get(HttpHeaders.AUTHORIZATION);

        var headersResponse = exchange.getResponse().getHeaders();

        if (Objects.nonNull(headerAuthorization)) {

            String token = Utility.getToken(headerAuthorization);

            securityService.validateToken(token)
                    .subscribe(validateResponse -> {

                        if (validateResponse != null) {
                            headersResponse.setBearerAuth(token);
                        }

                    }).dispose();
        }

        return chain.filter(exchange);
    }
}
