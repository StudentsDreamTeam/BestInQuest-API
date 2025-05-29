package com.github.StudentsDreamTeam.user;

import static org.mockito.Mockito.*;

import com.github.StudentsDreamTeam.dto.UpdateUserProfileDTO;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.LevelRequirementRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class UserServiceTests {

    private static final LocalDateTime now = LocalDateTime.now();
    private static final LocalDateTime yesterday = now.minusDays(1);
    private static final LocalDateTime longTimeAgo = now.minusYears(2).minusDays(5).minusMonths(1);

    @Test
    @DisplayName("Normal registration")
    public void normalRegistration() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        LevelRequirementRepository mockLevelRequirementRepository = mock(LevelRequirementRepository.class);

        UserService userService = new UserService(mockUserRepository, mockLevelRequirementRepository);

        User expected = new User();
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(0);

        when(mockUserRepository.save(expected)).thenReturn(expected);
        when(mockUserRepository.existsByEmail(expected.getEmail())).thenReturn(false);

        User actual = userService.registerUser(expected);

        verify(mockUserRepository).save(expected);
        verify(mockUserRepository).existsByEmail(expected.getEmail());
        verifyNoMoreInteractions(mockUserRepository);

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
        UserRepository mockUserRepository = mock(UserRepository.class);
        LevelRequirementRepository mockLevelRequirementRepository = mock(LevelRequirementRepository.class);

        UserService userService = new UserService(mockUserRepository, mockLevelRequirementRepository);

        User expected = new User();
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(0);

        when(mockUserRepository.save(expected)).thenReturn(expected);
        when(mockUserRepository.existsByEmail(expected.getEmail())).thenReturn(true);

        Assertions.assertThrows(IllegalStateException.class, () -> userService.registerUser(expected));

        verify(mockUserRepository).existsByEmail(expected.getEmail());
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("Normal flow of changing user profile info")
    public void updateProfileInfo() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        LevelRequirementRepository mockLevelRequirementRepository = mock(LevelRequirementRepository.class);

        UserService userService = new UserService(mockUserRepository, mockLevelRequirementRepository);

        User old = new User();
        old.setName("Juan");
        old.setXp(120);
        old.setLevel(1);
        old.setLastInDate(now);
        old.setRegistrationDate(now);
        old.setEmail("example@example.org");
        old.setPassword("some strong password");
        old.setStreak(0);

        User expected = new User();
        expected.setName("Quixote");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("changed@example.org");
        expected.setPassword("different password");
        expected.setStreak(0);

        when(mockUserRepository.findById(0L)).thenReturn(Optional.of(old));
        when(mockUserRepository.save(expected)).thenReturn(expected);
        when(mockUserRepository.existsByEmail(old.getEmail())).thenReturn(true);
        when(mockUserRepository.existsByEmail(expected.getEmail())).thenReturn(false);

        User actual = userService.updateUserProfile(0,
                new UpdateUserProfileDTO("Quixote", "changed@example.org", "different password"));

        verify(mockUserRepository).findById(0L);
        verify(mockUserRepository).existsByEmail(expected.getEmail());
        verify(mockUserRepository).save(expected);

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
    @DisplayName("Setting email that already exists")
    public void invalidUpdateProfileInfo() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        LevelRequirementRepository mockLevelRequirementRepository = mock(LevelRequirementRepository.class);

        UserService userService = new UserService(mockUserRepository, mockLevelRequirementRepository);

        User old = new User();
        old.setName("Juan");
        old.setXp(120);
        old.setLevel(1);
        old.setLastInDate(now);
        old.setRegistrationDate(now);
        old.setEmail("example@example.org");
        old.setPassword("some strong password");
        old.setStreak(0);

        User expected = new User();
        expected.setName("Quixote");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("changed@example.org");
        expected.setPassword("different password");
        expected.setStreak(0);

        when(mockUserRepository.findById(0L)).thenReturn(Optional.of(old));
        when(mockUserRepository.save(expected)).thenReturn(expected);
        when(mockUserRepository.existsByEmail(old.getEmail())).thenReturn(true);
        when(mockUserRepository.existsByEmail(expected.getEmail())).thenReturn(true);

        Assertions.assertThrows(IllegalStateException.class, () -> userService.updateUserProfile(0,
                new UpdateUserProfileDTO("Quixote", "changed@example.org", "different password")));

        verify(mockUserRepository).findById(0L);
        verify(mockUserRepository).existsByEmail(expected.getEmail());
        verifyNoMoreInteractions(mockUserRepository);

    }

    @Test
    @DisplayName("Checks if streak updates")
    public void checkStreakUpdate() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        LevelRequirementRepository mockLevelRequirementRepository = mock(LevelRequirementRepository.class);

        UserService userService = new UserService(mockUserRepository, mockLevelRequirementRepository);

        User old = new User();
        old.setName("Juan");
        old.setXp(120);
        old.setLevel(1);
        old.setLastInDate(yesterday);
        old.setRegistrationDate(longTimeAgo);
        old.setEmail("example@example.org");
        old.setPassword("some strong password");
        old.setStreak(10);

        User expected = new User();
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setRegistrationDate(longTimeAgo);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(11);

        when(mockUserRepository.findById(0L)).thenReturn(Optional.of(old));
        when(mockUserRepository.save(argThat(arg -> Objects.equals(expected.getId(), arg.getId())))).thenReturn(expected);

        User actual = userService.getUserProfile(0L);

        verify(mockUserRepository).findById(0L);
        verify(mockUserRepository).save(argThat(
                argument -> expected.getName().equals(argument.getName()) &&
                                expected.getXp().equals(argument.getXp()) &&
                                expected.getLevel().equals(argument.getLevel()) &&
                                expected.getRegistrationDate().equals(argument.getRegistrationDate()) &&
                                expected.getEmail().equals(argument.getEmail()) &&
                                expected.getPassword().equals(argument.getPassword()) &&
                                expected.getStreak().equals(argument.getStreak())));

        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getXp(), actual.getXp());
        Assertions.assertEquals(expected.getLevel(), actual.getLevel());
        Assertions.assertEquals(expected.getRegistrationDate(), actual.getRegistrationDate());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getStreak(), actual.getStreak());

    }
}
