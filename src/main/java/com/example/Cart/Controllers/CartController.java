package com.example.Cart.Controllers;

import com.example.Cart.Infra.Entities.CartEntity;
import com.example.Cart.Infra.Entities.CartRequest;
import com.example.Cart.Infra.Entities.ItemEntity;
import com.example.Cart.Infra.Entities.ItemRemove;
import com.example.Cart.Infra.Services.CartService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping(path = "/addItem")
    public ResponseEntity<CartEntity> addItem(@RequestBody @Valid CartRequest req, HttpServletResponse response) throws InterruptedException {
        return ResponseEntity.ok(cartService.addItem(req.getcartId(),req.getItemId(),req.getQuantidade(),response));
    }

    @GetMapping(path = "/showCart/{id}")
    public ResponseEntity<CartEntity> showCart(@PathVariable("id") String id, HttpServletResponse response){
        return ResponseEntity.ok(cartService.showCart(id,response));
    }

    @PutMapping(path = "/removeItem/{id}")
    public ResponseEntity<CartEntity> removeItem(@PathVariable("id") String id, @RequestBody ItemRemove itemId, HttpServletResponse response){
        return ResponseEntity.ok(cartService.removeItem(id,itemId.getItemId(),response));
    }

    @DeleteMapping(path = "/deleteCart/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable("id") String id, HttpServletResponse response){
        return ResponseEntity.ok(cartService.deleteCart(id,response));
    }

}
