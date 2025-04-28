package com.example.Cart.Infra.exceptions;

public class itemNotFoundException extends RuntimeException {
  public itemNotFoundException(){}
  public itemNotFoundException(String message){
    super(message);
  }
}
