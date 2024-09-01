package com.pe.relari.dao.api;

import com.pe.relari.model.api.TokenRequest;
import com.pe.relari.model.api.ValidateResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SecurityAuthenticationApi {

//    @POST("/srv/neg/v1/security-authentication/validate")
//    Single<ValidateResponse> validate(
//            @Body TokenRequest request
//    );

    @POST("/srv/neg/v1/security-authentication/validate")
    Call<ValidateResponse> validate(@Body TokenRequest request);

}
