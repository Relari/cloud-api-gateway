package com.pe.relari.util;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class Utility {

    private Utility () {}

    public static String getToken(List<String> headerAuthorization) {
        return headerAuthorization.stream()
                .findFirst()
                .map(Utility::getTokenFromHeader)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.CONFLICT, "No contiene el valor del token")
                );
    }

    public static String getTokenFromHeader(String tokenHeader) {
        if (tokenHeader == null) {
            return null;
        }
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(Constants.TOKEN_HEADER)) {
            return tokenHeader.substring(Constants.TOKEN_HEADER.length());
        }
        return null;
    }

}
