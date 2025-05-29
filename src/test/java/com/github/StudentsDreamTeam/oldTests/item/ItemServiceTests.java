package com.github.StudentsDreamTeam.item;

import static org.mockito.Mockito.*;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.enums.ItemRarity;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

    private static final Duration hour = Duration.ofHours(1);

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private static Item getSampleItem(Integer id) {
        Item sample = new Item();

        sample.setId(id);
        sample.setDuration(hour);
        sample.setName("test");
        sample.setDescription("test");
        sample.setCurrencyMultiplier(1.5f);
        sample.setXpMultiplier(1.5f);
        sample.setRarity(ItemRarity.EPIC.getValue());

        return sample;
    }

    @Test
    public void creationTest() {
        Item item = getSampleItem(0);
        ItemDTO itemDTO = ItemDTO.fromORM(item);

        when(itemRepository.existsByName(item.getName())).thenReturn(false);
        when(itemRepository.save(any())).thenReturn(item);

        itemService.create(itemDTO);

        verify(itemRepository).existsByName(item.getName());
        verify(itemRepository).save(any());
        verifyNoMoreInteractions(itemRepository);

    }

}
