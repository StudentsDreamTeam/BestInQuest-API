package com.github.StudentsDreamTeam;

import com.github.StudentsDreamTeam.model.LevelRequirement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.LevelRequirementRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class UserServiceTests {
    @Test
    @DisplayName("Normal registration")
    public void normalRegistration() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        LevelRequirementRepository mockLevelRequirementRepository = Mockito.mock(LevelRequirementRepository.class);

        UserService userService = new UserService(mockUserRepository, mockLevelRequirementRepository);

        LocalDateTime now = LocalDateTime.now();

        User expected = new User();
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(0);

        Mockito.when(mockUserRepository.save(expected)).thenReturn(expected);
        Mockito.when(mockUserRepository.existsByEmail(expected.getEmail())).thenReturn(false);

        User actual = userService.registerUser(expected);

        Mockito.verify(mockUserRepository).save(expected);
        Mockito.verify(mockUserRepository).existsByEmail(expected.getEmail());
        Mockito.verifyNoMoreInteractions(mockUserRepository);

        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getXp(), actual.getXp());
        Assertions.assertEquals(expected.getLevel(), actual.getLevel());
        Assertions.assertEquals(expected.getLastInDate(), actual.getLastInDate());
        Assertions.assertEquals(expected.getRegistrationDate(), actual.getRegistrationDate());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getStreak(), actual.getStreak());

    }

    @Test
    @DisplayName("Duplicate email deny of registration")
    public void duplicateEmail() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        LevelRequirementRepository mockLevelRequirementRepository = Mockito.mock(LevelRequirementRepository.class);

        UserService userService = new UserService(mockUserRepository, mockLevelRequirementRepository);

        LocalDateTime now = LocalDateTime.now();

        User expected = new User();
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(0);

        Mockito.when(mockUserRepository.save(expected)).thenReturn(expected);
        Mockito.when(mockUserRepository.existsByEmail(expected.getEmail())).thenReturn(true);

        Assertions.assertThrows(IllegalStateException.class, () -> userService.registerUser(expected));

        Mockito.verify(mockUserRepository).existsByEmail(expected.getEmail());
        Mockito.verifyNoMoreInteractions(mockUserRepository);
    }
}
