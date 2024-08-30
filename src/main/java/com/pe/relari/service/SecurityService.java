package com.pe.relari.service;

import com.pe.relari.model.api.ValidateResponse;
import io.reactivex.Single;

public interface SecurityService {

    Single<ValidateResponse> validateToken(String token);

}
