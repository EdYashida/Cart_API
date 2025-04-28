package com.example.Cart.dtos;

public record ProdExistenteDto(String cartId,
                               String productId,
                               boolean exists) {
}
