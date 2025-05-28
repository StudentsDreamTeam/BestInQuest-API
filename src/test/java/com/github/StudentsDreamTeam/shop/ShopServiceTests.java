package com.github.StudentsDreamTeam.shop;

import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.ShopRepository;
import com.github.StudentsDreamTeam.service.ShopService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ShopServiceTests {
    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ShopService shopService;
}
