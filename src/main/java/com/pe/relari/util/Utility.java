package com.pe.relari.util;

import org.springframework.util.StringUtils;

public class Utility {

    private Utility () {}

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
