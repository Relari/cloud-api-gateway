package com.pe.relari.config;

import com.pe.relari.dao.api.SecurityAuthenticationApi;
import com.pe.relari.util.LoggingLevelCatalog;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Lazy
@Configuration
public class RestConfiguration {

  private final SecurityAuthProperties securityAuthProperties;

  @Value("${logging.level.com.pe.relari}")
  String levelLogApp;

  public RestConfiguration(SecurityAuthProperties securityAuthProperties) {
    this.securityAuthProperties = securityAuthProperties;
  }

  private Retrofit retrofit() {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    validateLoggingLevel(logging);

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.addInterceptor(logging);

    return new Retrofit.Builder()
        .baseUrl(securityAuthProperties.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpClient.build())
        .build();
  }

  private void validateLoggingLevel(HttpLoggingInterceptor logging) {
    switch (LoggingLevelCatalog.valueOf(levelLogApp)) {
      case DEBUG:
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        break;
      case TRACE:
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        break;
      default:
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        break;
    }
  }

  @Bean
  public SecurityAuthenticationApi securityAuthenticationApi() {
    return retrofit().create(SecurityAuthenticationApi.class);
  }
}
