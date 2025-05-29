package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_inventory")
@Getter
@Setter
public class UsersInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item", nullable = false)
    private Item item;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "acquire_date", nullable = false)
    private LocalDateTime acquireDate;

    public static UsersInventory fromDTO(
            UsersInventoryDTO dto,
            User user,
            Item item
    ) {
        UsersInventory usersInventory = new UsersInventory();
        usersInventory.setId(dto.id());
        usersInventory.setUser(user);
        usersInventory.setItem(item);
        usersInventory.setAmount(dto.amount());
        usersInventory.setAcquireDate(dto.acquireDate());
        return usersInventory;
    }
}
