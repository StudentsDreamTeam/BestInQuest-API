package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Clan;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.ClanRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClanServiceTest {
    
    private ClanService clanService;
    private ClanRepository clanRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        clanRepository = mock(ClanRepository.class);
        userRepository = mock(UserRepository.class);
        clanService = new ClanService();
        // Используем рефлексию для установки mock-объектов
        try {
            var clanRepoField = ClanService.class.getDeclaredField("clanRepository");
            var userRepoField = ClanService.class.getDeclaredField("userRepository");
            clanRepoField.setAccessible(true);
            userRepoField.setAccessible(true);
            clanRepoField.set(clanService, clanRepository);
            userRepoField.set(clanService, userRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateClan() {
        // Подготовка данных
        Long userId = 1L;
        User leader = new User();
        leader.setId(1);
        
        Clan clan = new Clan();
        clan.setName("Test Clan");
        
        // Настройка поведения моков
        when(userRepository.findById(userId)).thenReturn(Optional.of(leader));
        when(clanRepository.save(any(Clan.class))).thenReturn(clan);

        // Вызов тестируемого метода
        Clan createdClan = clanService.createClan(clan, userId);

        // Проверки
        assertNotNull(createdClan);
        assertEquals("Test Clan", createdClan.getName());
        assertEquals(leader, createdClan.getLeader());
        
        // Проверка вызовов методов репозиториев
        verify(userRepository).findById(userId);
        verify(clanRepository).save(clan);
    }

    @Test
    void testCreateClanWithNonexistentUser() {
        Long userId = 999L;
        Clan clan = new Clan();
        clan.setName("Test Clan");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clanService.createClan(clan, userId));
        verify(userRepository).findById(userId);
        verify(clanRepository, never()).save(any(Clan.class));
    }

    @Test
    void testGetClanInfo() {
        Long clanId = 1L;
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Test Clan");

        when(clanRepository.findById(clanId)).thenReturn(Optional.of(clan));

        Clan foundClan = clanService.getClanInfo(clanId);

        assertNotNull(foundClan);
        assertEquals(clan.getId(), foundClan.getId());
        assertEquals(clan.getName(), foundClan.getName());
        verify(clanRepository).findById(clanId);
    }

    @Test
    void testGetNonexistentClanInfo() {
        Long clanId = 999L;

        when(clanRepository.findById(clanId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clanService.getClanInfo(clanId));
        verify(clanRepository).findById(clanId);
    }
} 