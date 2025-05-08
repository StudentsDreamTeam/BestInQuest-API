package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    boolean existsByName(String name);
}
