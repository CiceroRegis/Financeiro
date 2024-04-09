package com.financeiro.exceptions;

import org.springframework.http.HttpStatus;


public class RuntimeValidationException extends RuntimeException {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;

    private final Boolean sucesso;

    public RuntimeValidationException(String message) {
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.status = HttpStatus.PRECONDITION_FAILED.value();
        this.error = HttpStatus.PRECONDITION_FAILED.getReasonPhrase();
        this.message = message;
        this.sucesso = false;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSucesso() {
        return sucesso;
    }
}
