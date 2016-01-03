package com.brave.mystery.repository;

import com.brave.mystery.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByAvailableTrue();
    List<Item> findByAvailableFalse();
}