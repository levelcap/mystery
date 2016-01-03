package com.brave.mystery.repository;

import com.brave.mystery.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByEmailIgnoreCase(String email);
}