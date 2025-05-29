package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    @Query(value = """
        SELECT * FROM achievements a
        WHERE NOT EXISTS (
            SELECT 1 FROM users_achievements ua
            WHERE ua.achievement = a.id AND ua.user_id = :userId
        )
        """, nativeQuery = true)
    List<Achievement> findAchievementsNotOwnedByUserNative(@Param("userId") Integer userId);

    boolean existsByName(String name);
}
