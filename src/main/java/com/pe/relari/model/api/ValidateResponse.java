package com.pe.relari.model.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateResponse {

    private String dateExpiration;
    private Boolean isTokenExpired;

}