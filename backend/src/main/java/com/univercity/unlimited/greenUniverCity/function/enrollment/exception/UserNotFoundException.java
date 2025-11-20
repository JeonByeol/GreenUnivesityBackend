package com.univercity.unlimited.greenUniverCity.function.enrollment.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}