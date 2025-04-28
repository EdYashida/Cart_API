package com.example.Cart.Infra.Entities;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;


public class CartRequest {

    public CartRequest() {
    }

    @Id
    @NotNull
    private String cartId;
    private String itemId;
    private int quantidade;

    public CartRequest(String id, String itemId, int quantidade) {
        this.cartId = id;
        this.itemId = itemId;
        this.quantidade = quantidade;
    }

    public String getcartId() {
        return cartId;
    }

    public void setcartId(String id) {
        this.cartId = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
