package com.pe.relari.filter;

import com.pe.relari.model.api.ValidateResponse;
import com.pe.relari.model.error.ErrorResponse;
import com.pe.relari.service.SecurityService;
import com.pe.relari.util.ErrorCatalog;
import com.pe.relari.util.JsonConverter;
import com.pe.relari.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomErrorFilter extends AbstractGatewayFilterFactory<CustomErrorFilter.Config> {

    @Autowired
    SecurityService securityService;

    public CustomErrorFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            var request = exchange.getRequest();
            String tokenHeader = request.getHeaders().getFirst("Authorization");

            if (tokenHeader == null) {
                return handleUnauthorized(exchange, HttpStatus.UNAUTHORIZED, ErrorCatalog.TOKEN_NOT_FOUND);
            }

            if (isValidToken(tokenHeader)) {
                return handleUnauthorized(exchange, HttpStatus.UNAUTHORIZED, ErrorCatalog.TOKEN_INVALID_OR_EXPIRE);
            }

            return chain.filter(exchange);
        };
    }

    private boolean isValidToken(String tokenHeader) {
        String token = Utility.getTokenFromHeader(tokenHeader);
        ValidateResponse response = securityService.validateToken(token);
        return response == null;
    }

    private Mono<Void> handleUnauthorized(
            ServerWebExchange exchange, HttpStatus status, ErrorCatalog errorCatalog) {

        exchange.getResponse().setStatusCode(status);

        // Configurar el cuerpo de la respuesta
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorResponse = JsonConverter.convertToJson(
                new ErrorResponse(errorCatalog)
        );
        byte[] bytes = errorResponse.getBytes();

        // Escribir el cuerpo de la respuesta
        return exchange.getResponse()
                .writeWith(
                        Mono.fromCallable(() -> exchange.getResponse().bufferFactory().wrap(bytes)
                ));
    }

    public static class Config {
        // Configuración adicional si es necesario
    }
}
