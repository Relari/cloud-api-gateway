package com.pe.relari.model.error;

import com.pe.relari.util.ErrorCatalog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

        private String code;
        private String message;

        public ErrorResponse(ErrorCatalog errorCatalog) {
                this.code = errorCatalog.getCode();
                this.message = errorCatalog.getMessage();
        }

}
