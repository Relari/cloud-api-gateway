package com.pe.relari.filter;

import com.pe.relari.config.SecurityAuthProperties;
import com.pe.relari.service.SecurityService;
import com.pe.relari.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    SecurityService securityService;

    @Autowired
    SecurityAuthProperties securityAuthProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        var request = exchange.getRequest();

        var headersRequest = request.getHeaders();
        var headerAuthorization = headersRequest.get(HttpHeaders.AUTHORIZATION);

        boolean validatePath = securityAuthProperties.validate(
                request.getMethodValue(), request.getURI().getPath()
        );

        if (Objects.isNull(headerAuthorization) && !validatePath) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No contiene el token");
        }

        var headersResponse = exchange.getResponse().getHeaders();

        if (Objects.nonNull(headerAuthorization) && validatePath) {

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
