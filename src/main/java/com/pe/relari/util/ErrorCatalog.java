package com.pe.relari.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCatalog {

    TOKEN_INVALID_OR_EXPIRE("API401", "Invalid or expired token."),
    TOKEN_NOT_FOUND("API401", "Token not found.");

    private final String code;
    private final String message;

}
