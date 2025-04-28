package com.example.Cart.Infra.exceptions;

public class cartNotFoundException extends RuntimeException {
    public cartNotFoundException(){}
    public cartNotFoundException(String message){
        super(message);
    }
}
