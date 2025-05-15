package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.UsersInventory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public record UsersInventoryDTO(
        Integer id,
        Integer userId,
        Integer itemId,
        Long amount,
        LocalDateTime acquireDate
) {
    public static UsersInventoryDTO fromORM(UsersInventory usersInventory) {
        return new UsersInventoryDTO(
                usersInventory.getId(),
                usersInventory.getUser().getId(),
                usersInventory.getItem().getId(),
                usersInventory.getAmount(),
                usersInventory.getAcquireDate()
        );
    }
}
