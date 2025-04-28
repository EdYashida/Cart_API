package com.example.Cart.Infra.Services;

import com.example.Cart.Infra.Entities.CartEntity;
import com.example.Cart.Infra.Entities.ItemEntity;
import com.example.Cart.Infra.Repositories.cartRepo;
import com.example.Cart.Infra.exceptions.cartNotFoundException;
import com.example.Cart.Infra.exceptions.itemNotFoundException;
import com.example.Cart.consumers.CartConsumer;
import com.example.Cart.dtos.ProdExistenteDto;
import com.example.Cart.dtos.ProdutoRetornoDto;
import com.example.Cart.poducers.CartProducer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private cartRepo cartrepo;
    private CartProducer cartProducer;
    private CartConsumer cartConsumer;
    private CartEntity cartEntity;
   // private RabbitTemplate rabbitTemplate;


    /*
    @Value("${broker.rpc.produto.exchange}")
    private String exchange;

    @Value("${broker.rpc.produto.exists.routing}")
    private String routingExists;

    @Value("${broker.rpc.produto.details.routing}")
    private String routingDetails;
    */

    @Autowired
    public CartService(cartRepo cartrepo, CartProducer cartProducer,CartConsumer cartConsumer, RabbitTemplate rabbitTemplate){
        this.cartrepo = cartrepo;
        this.cartProducer = cartProducer;
        this.cartConsumer = cartConsumer;
        //this.rabbitTemplate = rabbitTemplate;
    }

    public CartService(){

    }

    /*
    public boolean verificarProdutoExiste(String cartID, String productId) {
        ProdExistenteDto dto = new ProdExistenteDto(cartID, productId, false);
        ProdExistenteDto resposta = (ProdExistenteDto) rabbitTemplate.convertSendAndReceive(exchange, routingExists, dto);
        return resposta != null && resposta.exists();
    }

    public ItemEntity buscarDetalhesProduto(String productId) {
        ProdutoRetornoDto resposta = (ProdutoRetornoDto) rabbitTemplate.convertSendAndReceive(exchange, routingDetails, productId);
        return new ItemEntity(resposta.productId(), resposta.nome(), resposta.preco());
    }
    */


    private void verifiCart(String cod, HttpServletResponse response){
        if (cod == null || cod.isEmpty() || cartrepo.findById(cod).isEmpty()) {
            if (response != null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            throw new cartNotFoundException("Carrinho não encontrado");
        }
    }

    public ItemEntity esperarItemChegar(String cartId, String itemId, int timeoutMs) throws InterruptedException {
        int waited = 0;
        int interval = 100; // intervalos de 100 ms

        while (waited < timeoutMs) {
            ItemEntity item = cartConsumer.getItem();
            String idCarDet = cartConsumer.getIdCarDet();

            if (item != null && idCarDet != null) {
                if (item.getId().equals(itemId) && idCarDet.equals(cartId)) {
                    return item; // Sucesso: item encontrado!
                }
            }

            Thread.sleep(interval);
            waited += interval;
        }

        throw new IllegalStateException("Timeout esperando o item chegar via RabbitMQ.");
    }


    @Transactional
    public CartEntity addItem(String cart, String itemId, int quant, HttpServletResponse response) throws InterruptedException {

        //Se carrinho do código passado não existe, instancia com valor zerado e id passado
        if (cartrepo.findById(cart).isEmpty()){
            cartEntity = new CartEntity(cart);
        }
        //se não, recupera o carrinho
        else {
            cartEntity = cartrepo.findById(cart).orElse(null);
        }

        cartProducer.requireMessageProd(cart,itemId);

        /*
        boolean existe = true;

        while (!cartConsumer.getExiste() || !cartConsumer.getIdCarEx().equals(cart)){
            Thread.sleep(500);
            if (cartConsumer.getExiste() && cartConsumer.getIdCarEx().equals(cart)){
                 existe = true;
                break;
            }
        }

        if (!existe){
            if (response != null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            throw new itemNotFoundException("Produto não encontrado");
        }
        */


        ItemEntity item = esperarItemChegar(cart,itemId,5000);
        /*
        while (cartConsumer.getItem()==null || !cartConsumer.getIdCarDet().equals(cart)){
            Thread.sleep(500);
            if (cartConsumer.getItem()!=null) {
                if (cartConsumer.getItem().getId() == itemId && cartConsumer.getIdCarDet().equals(cart)) {
                    break;
                   //item = cartConsumer.getItem();
                }
            }
        }
        item = cartConsumer.getItem();


        boolean existe = verificarProdutoExiste(cart, itemId);
        if (!existe){
            throw new itemNotFoundException("O item requerido não está registrado!");
        }

        ItemEntity item = buscarDetalhesProduto(itemId);
        */

        item.roundPrice();
        item.setQuantidade(quant);

        cartEntity.addItens(item);

        //altera valor considerando itens e suas quantidades
        BigDecimal precoItem = cartEntity.getLastItem();
        cartEntity.addValor(precoItem);

        cartConsumer.setItem(null);
        //cartConsumer.setExiste(false);
        return cartrepo.save(cartEntity);
    }

    public CartEntity showCart(String cod, HttpServletResponse response) {
            verifiCart(cod,response);

            return cartrepo.findById(cod).orElseThrow(() -> new cartNotFoundException("Carrinho não encontrado!"));
    }

    public CartEntity removeItem(String cartId, String itemID, HttpServletResponse response){
        verifiCart(cartId,response);

        CartEntity cartAtual = cartrepo.findById(cartId).orElse(null);

        int remover = cartAtual.findById(itemID);
        cartAtual.getItens().remove(remover);

        cartrepo.deleteById(cartId);

        return cartrepo.save(cartAtual);
    }

    public String deleteCart(String cartId, HttpServletResponse response){
        verifiCart(cartId,response);

        cartrepo.deleteById(cartId);

        return "O carrinho foi deletado com êxito!";
    }


}
