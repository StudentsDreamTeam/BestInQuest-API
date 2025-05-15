package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.ShopDTO;
import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import com.github.StudentsDreamTeam.enums.Availability;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shop", uniqueConstraints = @UniqueConstraint(columnNames = "item"))
@Getter
@Setter
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item", nullable = false)
    private Item item;

    @Column(name = "cost", nullable = false)
    private Long cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false)
    private Availability availability;

    public static Shop fromDTO(
            ShopDTO shopDTO,
            Item item
    ) {
        Shop shop = new Shop();
        shop.setId(shopDTO.id());
        shop.setItem(item);
        shop.setCost(shopDTO.cost());
        shop.setAvailability(Availability.fromValue(shopDTO.availability()));
        return shop;
    }
}

