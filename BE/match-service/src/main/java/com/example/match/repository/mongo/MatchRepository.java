package com.example.match.repository.mongo;

import com.example.match.entity.mongo.MatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchRepository extends MongoRepository<MatchEntity, UUID> {
    @Query("{$or: [{'redUserId': ?1}, {'blackUserId': ?1}, {'?1': null}], 'result': {$nin: ['PLAYING...', 'Match cancel.']}}")
    Page<MatchEntity> findAllFinished(Pageable pageable, String userId);
}
