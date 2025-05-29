package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.UsersInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersInventoryRepository extends JpaRepository<UsersInventory, Integer> {
        Optional<UsersInventory> findByUserIdAndItemId(Integer userId, Integer itemId);
        List<UsersInventory> findByUserId(Integer userId);
}
