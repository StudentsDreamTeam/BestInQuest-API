package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    Optional<Shop> findByItemId(Integer itemId);
}
