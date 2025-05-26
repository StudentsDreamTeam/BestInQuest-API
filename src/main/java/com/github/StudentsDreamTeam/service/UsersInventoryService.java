package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import com.github.StudentsDreamTeam.model.Income;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UsersInventory;
import com.github.StudentsDreamTeam.repository.IncomeRepository;
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
    private UserService userService;

    @Autowired
    private UsersInventoryRepository inventoryRepo;

    @Autowired
    private AchievementDetector achievementDetector;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private IncomeRepository incomeRepo;

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
        achievementDetector.detectForUser(user);

        return fromORM(inventoryRepo.save(inventory));
    }

    @Transactional
    public void removeItemFromInventory(Integer userId, Integer itemId) {
        UsersInventory inventory = inventoryRepo.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in user's inventory"));
        inventoryRepo.delete(inventory);
    }

    @Transactional
    public void sellItemFromInventory(Integer userId, Integer itemId) {
        User user = userRepo.findById(Long.valueOf(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UsersInventory inventory = inventoryRepo.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in user's inventory"));

        Item item = inventory.getItem();
        long salePrice = item.getCost();
        achievementDetector.detectForUser(user);
        userService.updateUserLevel(user);

        incomeRepo.save(new Income(
                user,
                (int) salePrice,
                LocalDateTime.now(), "Item sold: " + item.getName()
        ));

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
