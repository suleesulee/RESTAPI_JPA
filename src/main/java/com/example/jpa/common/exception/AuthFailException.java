package com.example.jpa.common.exception;

public class AuthFailException extends RuntimeException {
    public AuthFailException(String msg) {
        super(msg);
    }
}
