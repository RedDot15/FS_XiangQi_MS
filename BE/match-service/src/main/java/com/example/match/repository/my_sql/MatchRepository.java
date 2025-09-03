package com.example.match.repository.my_sql;

import com.example.match.entity.my_sql.MatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {
    @Query("SELECT m FROM MatchEntity m WHERE (:userId IS NULL OR m.blackUserId.id = :userId OR m.redUserId.id = :userId) AND m.result NOT IN ('PLAYING...', 'Match cancel.')")
    Page<MatchEntity> findAllFinished(Pageable pageable, @Param("userId") Long userId);
}
