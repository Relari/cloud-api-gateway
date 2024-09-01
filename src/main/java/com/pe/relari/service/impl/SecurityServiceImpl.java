package com.pe.relari.service.impl;

import com.pe.relari.dao.api.SecurityAuthenticationApi;
import com.pe.relari.model.api.TokenRequest;
import com.pe.relari.model.api.ValidateResponse;
import com.pe.relari.service.SecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    SecurityAuthenticationApi authenticationApi;

//    @Override
//    public Single<ValidateResponse> validateToken(String token) {
//        return Single.fromCallable(() -> new TokenRequest(token))
//                .flatMap(authenticationApi::validate)
//                .doOnError(throwable -> log.error("Error en la autenticacion", throwable))
//                .filter(validateResponse ->
//                        Boolean.FALSE.equals(validateResponse.getIsTokenExpired())
//                )
//                .toSingle();
//    }

    @Override
    public ValidateResponse validateToken(String token) {

        Response<ValidateResponse> response;

        try {
            response = authenticationApi.validate(new TokenRequest(token))
                    .execute();
        } catch (IOException e) {
            log.error("Invalid or expired token.", e);
            return null;
        }

        if (response.isSuccessful()) {
            log.debug("Token validated.");
            return response.body();
        }

        return null;
    }
}
