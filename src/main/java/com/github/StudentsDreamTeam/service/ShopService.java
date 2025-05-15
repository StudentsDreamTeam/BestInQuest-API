package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.ShopDTO;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.Shop;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<ShopDTO> getAllShopItems() {
        return shopRepository.findAll()
                .stream()
                .map(ShopDTO::fromORM)
                .toList();
    }

    @Transactional
    public ShopDTO addToShop(ShopDTO dto) {
        Item item = itemRepository.findById(dto.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        Shop shop = Shop.fromDTO(dto, item);
        return ShopDTO.fromORM(shopRepository.save(shop));
    }

    @Transactional
    public void removeFromShop(Integer itemId) {
        Shop shop = shopRepository.findByItemId(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Shop item not found"));

        shopRepository.delete(shop);
    }

    @Transactional
    public ShopDTO updatePrice(Integer itemId, Long newCost) {
        Shop shop = shopRepository.findByItemId(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Shop item not found"));

        shop.setCost(newCost);
        return ShopDTO.fromORM(shopRepository.save(shop));
    }
}
