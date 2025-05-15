package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UsersInventory;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.repository.UsersInventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.StudentsDreamTeam.dto.UsersInventoryDTO.fromORM;

@Service
public class UsersInventoryService {

    @Autowired
    private UsersInventoryRepository inventoryRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;


    public List<UsersInventoryDTO> getAll() {
        return inventoryRepo.findAll().stream().map(UsersInventoryDTO::fromORM).toList();
    }

    @Transactional
    public UsersInventoryDTO addItemToInventory(UsersInventoryDTO usersInventoryDTO) {
        User user = userRepo.findById(usersInventoryDTO.userId().longValue())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Item item = itemRepo.findById(usersInventoryDTO.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        UsersInventory inventory = new UsersInventory();
        inventory.setUser(user);
        inventory.setItem(item);
        inventory.setAmount(usersInventoryDTO.amount());
        inventory.setAcquireDate(LocalDateTime.now());

        return fromORM(inventoryRepo.save(inventory));
    }

    @Transactional
    public void removeItemFromInventory(Integer userId, Integer itemId) {
        UsersInventory inventory = inventoryRepo.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in user's inventory"));
        inventoryRepo.delete(inventory);
    }

    public UsersInventoryDTO getById(Integer id) {
        return inventoryRepo.findById(id)
                .map(UsersInventoryDTO::fromORM)
                .orElseThrow(() -> new EntityNotFoundException("Inventory entry not found"));
    }

    public List<UsersInventoryDTO> getInventoryByUserId(Integer userId) {
        List<UsersInventory> inventory = inventoryRepo.findByUserId(userId);

        return inventory.stream()
                .map(UsersInventoryDTO::fromORM)
                .collect(Collectors.toList());
    }
}
