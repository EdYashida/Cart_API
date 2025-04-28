package com.example.Cart.Infra.Entities;

import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "produtoCart")
public class ItemEntity {

    @Id
    private String id;

    private String name;

    @Digits(integer = 4,fraction = 2)
    private BigDecimal price;

    private int quantidade=0;

    public ItemEntity(String id, String name, BigDecimal price){
        this.id=id;
        this.name=name;
        this.price=price;
        this.quantidade=0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void roundPrice(){
        this.price = this.price.setScale(2,BigDecimal.ROUND_UP);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
