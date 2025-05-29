package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
}
