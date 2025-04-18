package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Clan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanRepository extends JpaRepository<Clan, Long> {
}
