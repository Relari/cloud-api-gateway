package com.pe.relari.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Data
@Lazy
@Configuration
@ConfigurationProperties(prefix = "application.http-client.security-authentication")
public class SecurityAuthProperties {

    private String baseUrl;
    private List<ExcludePath> excludePaths;

    @Data
    public static class ExcludePath {
        private String method;
        private String path;

        public boolean validate(String method, String path) {
            return this.method.equals(method) && this.path.equals(path);
        }

    }

    public boolean validate(String method, String path) {
        boolean validatePath = true;
        for(var data : excludePaths) {
            if(data.validate(method, path)) {
                return validatePath;
            }
        }
        return false;
    }

}
