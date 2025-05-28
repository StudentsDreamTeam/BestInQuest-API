package com.github.StudentsDreamTeam.item;

import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.service.ItemService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ItemServiceTests {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;
}
