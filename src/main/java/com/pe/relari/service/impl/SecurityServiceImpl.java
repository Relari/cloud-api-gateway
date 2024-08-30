package com.pe.relari.service.impl;

import com.pe.relari.dao.api.SecurityAuthenticationApi;
import com.pe.relari.model.api.TokenRequest;
import com.pe.relari.model.api.ValidateResponse;
import com.pe.relari.service.SecurityService;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    SecurityAuthenticationApi authenticationApi;

    @Override
    public Single<ValidateResponse> validateToken(String token) {
        return Single.fromCallable(() -> new TokenRequest(token))
                .flatMap(authenticationApi::validate)
                .doOnError(throwable -> log.error("Error en la autenticacion", throwable))
                .filter(validateResponse ->
                        Boolean.FALSE.equals(validateResponse.getIsTokenExpired())
                )
                .toSingle();
    }
}
