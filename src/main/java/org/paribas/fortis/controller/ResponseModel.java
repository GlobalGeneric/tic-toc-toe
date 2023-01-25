package org.paribas.fortis.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {

    private Boolean hasError;

    private Integer errorCode;

    private String message;

    private T result;

    public ResponseModel(Integer errorCode, String message)
    {
        this.setHasError(true);
        this.setErrorCode(errorCode);
        this.setMessage(message);
    }
    public ResponseModel(T result)
    {
        this.setHasError(false);
        this.setResult(result);
    }
    public ResponseModel(Boolean hasError, T result)
    {
        this.setHasError(hasError);
        this.setResult(result);
    }
    public ResponseModel(Boolean hasError)
    {
        this.setHasError(hasError);
    }
    public ResponseModel(Boolean hasError, String message)
    {
        this.setHasError(hasError);
        this.setMessage(message);
    }

    public ResponseModel(T result, String message) {
        this.setHasError(false);
        this.setMessage(message);
        this.setResult( result);
    }
}