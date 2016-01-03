package com.brave.mystery.repository;

import com.brave.mystery.model.PuzzlePage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PuzzlePageRepository extends MongoRepository<PuzzlePage, String> {
}