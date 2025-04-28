package com.example.Cart.consumers;

import com.example.Cart.Infra.Entities.ItemEntity;
import com.example.Cart.dtos.ProdExistenteDto;
import com.example.Cart.dtos.ProdutoRetornoDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CartConsumer {

    //private boolean existe = false;
    private ItemEntity item=null;
    //private String idCarEx;
    private String idCarDet;

    public String getIdCarDet() {
        return idCarDet;
    }

    public void setIdCarDet(String idCarDet) {
        this.idCarDet = idCarDet;
    }


    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public ItemEntity getItem() {
        return item;
    }
    /*
        public String getIdCarEx() {
        return idCarEx;
    }

    public void setIdCarEx(String idCarEx) {
        this.idCarEx = idCarEx;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    public boolean getExiste() {
        return existe;
    }



    @RabbitListener (queues = "${broker.queue.prod.verificado}")
    public void listenProdExist(@Payload ProdExistenteDto prodExistenteDto){
        existe=prodExistenteDto.exists();
        idCarEx= prodExistenteDto.cartId();
    }
    */

    @RabbitListener (queues = "${broker.queue.prod.detalhado}")
    public void listenProdDetail(@Payload ProdutoRetornoDto produtoRetornoDto){
        item = new ItemEntity(produtoRetornoDto.productId(),produtoRetornoDto.nome(),produtoRetornoDto.preco());
        //item.setId(produtoRetornoDto.productId());
        //item.setName(produtoRetornoDto.nome());
        //item.setPrice(produtoRetornoDto.preco());
        idCarDet= produtoRetornoDto.cartId();
    }
}
