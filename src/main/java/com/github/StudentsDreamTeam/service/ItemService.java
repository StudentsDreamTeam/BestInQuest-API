package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<ItemDTO> getAll() {
        return itemRepository.findAll().stream().map(ItemDTO::fromORM).toList();
    }

    @Transactional
    public ItemDTO create(ItemDTO dto) {
        if (itemRepository.existsByName(dto.name())) {
            throw new IllegalStateException("Item with this name already exists");
        }
        Item saved = itemRepository.save(Item.fromDTO(dto));
        return ItemDTO.fromORM(saved);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item not found");
        }
        itemRepository.deleteById(id);
    }

    public ItemDTO getById(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        return ItemDTO.fromORM(item);
    }
}
