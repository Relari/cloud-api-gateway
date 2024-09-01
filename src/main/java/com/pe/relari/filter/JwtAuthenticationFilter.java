package com.pe.relari.filter;

import com.pe.relari.config.SecurityAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    SecurityAuthProperties securityAuthProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        var request = exchange.getRequest();
        var method = request.getMethodValue();
        var path = request.getURI().getPath();

        boolean isExcludedPath = securityAuthProperties.validate(
                method, path
        );

        if (isExcludedPath) {
            log.debug("Excluded route. [method={}, path={}]", method, path);
        } else {
            log.debug("Route with Token. [method={}, path={}]", method, path);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
