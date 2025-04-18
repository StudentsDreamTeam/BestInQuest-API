package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
//    List<Achievement> findByUserId(Long userId);
}
