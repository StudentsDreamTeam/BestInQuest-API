
package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Spendings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingsRepository extends JpaRepository<Spendings, Long> {
}
