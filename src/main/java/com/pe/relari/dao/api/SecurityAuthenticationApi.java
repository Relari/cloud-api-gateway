package com.pe.relari.dao.api;

import com.pe.relari.model.api.TokenRequest;
import com.pe.relari.model.api.ValidateResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SecurityAuthenticationApi {

    @POST("/srv/neg/v1/security-authentication/validate")
    Single<ValidateResponse> validate(@Body TokenRequest request);

}
