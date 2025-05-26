package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.LevelRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LevelRequirementRepository extends JpaRepository<LevelRequirement, Integer> {

    @Query("SELECT l FROM LevelRequirement l WHERE l.requiredXp <= :xp ORDER BY l.level DESC")
    List<LevelRequirement> findAllByXp(@Param("xp") int xp);

    @Query("SELECT l FROM LevelRequirement l WHERE l.level = :level + 1")
    Optional<LevelRequirement> findNextLevel(@Param("level") int level);
}

