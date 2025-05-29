package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.model.Clan;
import com.github.StudentsDreamTeam.model.Project;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.service.ClanService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClanControllerTest {

    @Mock
    private ClanService clanService;

    @InjectMocks
    private ClanController clanController;

    private Clan createTestClan() {
        User leader = new User();
        leader.setId(1);
        leader.setName("Test Leader");
        leader.setEmail("leader@example.com");

        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");
        project.setDescription("Test Project Description");
        project.setOwner(leader);
        project.setStatus("ACTIVE");
        project.setCreationDate(LocalDateTime.now());
        project.setProjectItems(0L);
        project.setDone(false);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Test Clan");
        clan.setLeader(leader);
        clan.setCreationDate(LocalDateTime.now());
        clan.setRating(100);
        clan.setProject(project);

        return clan;
    }

    @Test
    void createClan_Success() {
        // Arrange
        Clan testClan = createTestClan();
        Long userId = 1L;
        when(clanService.createClan(any(Clan.class), eq(userId))).thenReturn(testClan);

        // Act
        Clan result = clanController.createClan(testClan, userId);

        // Assert
        assertNotNull(result);
        assertEquals(testClan.getId(), result.getId());
        assertEquals(testClan.getName(), result.getName());
        assertEquals(testClan.getLeader().getId(), result.getLeader().getId());
        assertEquals(testClan.getRating(), result.getRating());
        assertEquals(testClan.getProject().getId(), result.getProject().getId());
        verify(clanService).createClan(any(Clan.class), eq(userId));
    }

    @Test
    void createClan_WhenUserNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Clan testClan = createTestClan();
        Long userId = 99L;
        when(clanService.createClan(any(Clan.class), eq(userId)))
            .thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> clanController.createClan(testClan, userId));
        verify(clanService).createClan(any(Clan.class), eq(userId));
    }

    @Test
    void getClanInfo_Success() {
        // Arrange
        Clan testClan = createTestClan();
        Long clanId = 1L;
        when(clanService.getClanInfo(clanId)).thenReturn(testClan);

        // Act
        Clan result = clanController.getClanInfo(clanId);

        // Assert
        assertNotNull(result);
        assertEquals(testClan.getId(), result.getId());
        assertEquals(testClan.getName(), result.getName());
        assertEquals(testClan.getLeader().getId(), result.getLeader().getId());
        assertEquals(testClan.getRating(), result.getRating());
        assertEquals(testClan.getProject().getId(), result.getProject().getId());
        verify(clanService).getClanInfo(clanId);
    }

    @Test
    void getClanInfo_WhenClanNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Long clanId = 99L;
        when(clanService.getClanInfo(clanId))
            .thenThrow(new EntityNotFoundException("Clan not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> clanController.getClanInfo(clanId));
        verify(clanService).getClanInfo(clanId);
    }
} 