package com.example.profile.repository;

import com.example.profile.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends Neo4jRepository<ProfileEntity, String> {
    Page<ProfileEntity> findAll(Pageable pageable);

    Optional<ProfileEntity> findByUserId(String userId);

    @Query("MATCH (p:profile) WHERE p.userId = $userId RETURN p.rating")
    Optional<Integer> findRatingByUserId(@Param("userId") String userId);
}
