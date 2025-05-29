package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UpdateUserProfileDTO;
import com.github.StudentsDreamTeam.model.LevelRequirement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.LevelRequirementRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private LevelRequirementRepository levelRequirementRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        levelRequirementRepository = mock(LevelRequirementRepository.class);
        userService = new UserService(userRepository, levelRequirementRepository);
    }

    @Test
    void testUpdateUserProfile() {
        // Подготовка данных
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@email.com");
        existingUser.setPassword("oldpass");

        UpdateUserProfileDTO dto = new UpdateUserProfileDTO(
            "New Name",
            "new@email.com",
            "newpass"
        );

        when(userRepository.findById(userId.longValue())).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Выполнение
        User updatedUser = userService.updateUserProfile(userId, dto);

        // Проверки
        assertNotNull(updatedUser);
        assertEquals(dto.name(), updatedUser.getName());
        assertEquals(dto.email(), updatedUser.getEmail());
        assertEquals(dto.password(), updatedUser.getPassword());
        
        verify(userRepository).findById(userId.longValue());
        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUserProfileWithExistingEmail() {
        // Подготовка данных
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@email.com");

        UpdateUserProfileDTO dto = new UpdateUserProfileDTO(
            null,
            "taken@email.com",
            null
        );

        when(userRepository.findById(userId.longValue())).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        // Проверка исключения
        assertThrows(IllegalStateException.class, () -> userService.updateUserProfile(userId, dto));
        
        verify(userRepository).findById(userId.longValue());
        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserLevel() {
        // Подготовка данных
        User user = new User();
        user.setId(1);
        user.setXp(1000);
        user.setLevel(1);

        LevelRequirement levelReq = new LevelRequirement();
        levelReq.setLevel(2);
        levelReq.setRequiredXp(1000);

        when(levelRequirementRepository.findAllByXp(user.getXp())).thenReturn(List.of(levelReq));
        when(userRepository.save(user)).thenReturn(user);

        // Выполнение
        userService.updateUserLevel(user);

        // Проверки
        assertEquals(2, user.getLevel());
        verify(levelRequirementRepository).findAllByXp(user.getXp());
        verify(userRepository).save(user);
    }

    @Test
    void testGetXpToNextLevel() {
        // Подготовка данных
        User user = new User();
        user.setLevel(1);
        user.setXp(800);

        LevelRequirement nextLevel = new LevelRequirement();
        nextLevel.setLevel(2);
        nextLevel.setRequiredXp(1000);

        when(levelRequirementRepository.findNextLevel(user.getLevel())).thenReturn(Optional.of(nextLevel));

        // Выполнение
        Integer xpToNext = userService.getXpToNextLevel(user);

        // Проверки
        assertEquals(200, xpToNext);
        verify(levelRequirementRepository).findNextLevel(user.getLevel());
    }

    @Test
    void testRegisterUser() {
        // Подготовка данных
        User newUser = new User();
        newUser.setEmail("new@email.com");
        newUser.setName("New User");
        newUser.setPassword("password");

        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Выполнение
        User registeredUser = userService.registerUser(newUser);

        // Проверки
        assertNotNull(registeredUser);
        assertEquals(newUser.getEmail(), registeredUser.getEmail());
        assertEquals(newUser.getName(), registeredUser.getName());
        
        verify(userRepository).existsByEmail(newUser.getEmail());
        verify(userRepository).save(newUser);
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        // Подготовка данных
        User newUser = new User();
        newUser.setEmail("existing@email.com");

        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(true);

        // Проверка исключения
        assertThrows(IllegalStateException.class, () -> userService.registerUser(newUser));
        
        verify(userRepository).existsByEmail(newUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserProfile() {
        // Подготовка данных
        Long userId = 1L;
        LocalDateTime originalLastInDate = LocalDateTime.now().minusDays(1);
        
        User user = new User();
        user.setId(1);
        user.setLastInDate(originalLastInDate);
        user.setStreak(1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            // Проверяем, что lastInDate был обновлен
            assertTrue(savedUser.getLastInDate().isAfter(originalLastInDate));
            return savedUser;
        });

        // Выполнение
        User result = userService.getUserProfile(userId);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.getStreak()); // Стрик должен увеличиться на 1
        assertNotNull(result.getLastInDate());
        assertTrue(result.getLastInDate().isAfter(originalLastInDate));
        
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void testGetUserProfileNotFound() {
        // Подготовка данных
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> userService.getUserProfile(userId));
        
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAll() {
        // Подготовка данных
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // Выполнение
        List<User> result = userService.getAll();

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
        
        verify(userRepository).findAll();
    }
} 