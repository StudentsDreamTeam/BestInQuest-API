package com.github.StudentsDreamTeam.shop;

import static org.mockito.Mockito.*;

import com.github.StudentsDreamTeam.dto.ShopDTO;
import com.github.StudentsDreamTeam.enums.Availability;
import com.github.StudentsDreamTeam.enums.ItemRarity;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.Shop;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.ShopRepository;
import com.github.StudentsDreamTeam.service.ShopService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTests {

    private static final Duration hour = Duration.ofHours(1);

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ShopService shopService;

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
    public void setItemForSaleTest() {

        ShopDTO argument = new ShopDTO(0, 0, 100L, Availability.AVAILABLE.getValue());

        Item item = getSampleItem(0);

        Shop expected = Shop.fromDTO(argument, item);

        when(itemRepository.findById(0)).thenReturn(Optional.of(item));
        when(shopRepository.save(any())).thenReturn(expected);

        shopService.addToShop(argument);

        verify(itemRepository).findById(0);
        verifyNoMoreInteractions(itemRepository);

        verify(shopRepository).save(any());
        verifyNoMoreInteractions(shopRepository);

    }

}
