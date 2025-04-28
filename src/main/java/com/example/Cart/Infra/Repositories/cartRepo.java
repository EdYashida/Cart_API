package com.example.Cart.Infra.Repositories;

import com.example.Cart.Infra.Entities.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface cartRepo extends MongoRepository<CartEntity, String> {
}
