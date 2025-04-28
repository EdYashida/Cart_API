package com.example.Cart.Infra.Entities;

import com.example.Cart.Infra.exceptions.itemNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Document (collection = "cart")
public class CartEntity {
    public CartEntity(){

    }

    public CartEntity(String id){
        this.id=id;
    }

    @Id
    private String id;
    private List<ItemEntity> itens = new ArrayList<>();
    private BigDecimal valor = BigDecimal.ZERO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemEntity> getItens() {
        return itens;
    }

    public void setItens(List<ItemEntity> itens) {
        this.itens = itens;
    }

    //adicionar itens
    public void addItens( ItemEntity item){
        this.itens.add(item);
    }

    public int findById(String achar){
        int count;
        for (count=0 ; count <= this.itens.size()-1 ; count++){
            ItemEntity procurado = this.itens.get(count);
            if(procurado.getId().equals(achar)){
                return count;
            }
        }
        throw new itemNotFoundException("O item informado não está no carrinho!");
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    //somar com valor atual
    public void addValor (BigDecimal val){
        this.valor = this.valor.add(val);
    }

    //recupera ultimo item adicionado à lista e retorna valor*quantidade
    public BigDecimal getLastItem(){
        ItemEntity LastItem = itens.get(itens.size() -1 );
        BigDecimal itemNumber = new BigDecimal(LastItem.getQuantidade());
        BigDecimal ExtraValue = LastItem.getPrice().multiply(itemNumber);
        return ExtraValue.setScale(2,BigDecimal.ROUND_UP);
    }
}
