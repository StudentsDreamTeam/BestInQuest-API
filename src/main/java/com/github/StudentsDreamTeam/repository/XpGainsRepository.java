package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.XpGains;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XpGainsRepository extends JpaRepository<XpGains, Long> {
}