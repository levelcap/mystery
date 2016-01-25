package com.brave.mystery.repository;

import com.brave.mystery.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {
    List<Character> findByNameLikeIgnoreCase(String name);
    List<Character> findByNpcTrue();
    List<Character> findByNpcFalse();
}