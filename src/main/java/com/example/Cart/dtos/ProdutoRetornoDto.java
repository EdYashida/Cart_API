package com.example.Cart.dtos;

import java.math.BigDecimal;

public record ProdutoRetornoDto(String cartId,
                                String productId,
                                String nome,
                                BigDecimal preco) {
}
