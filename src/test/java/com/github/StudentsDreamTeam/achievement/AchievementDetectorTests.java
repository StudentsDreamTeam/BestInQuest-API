package com.github.StudentsDreamTeam.achievement;

import static org.mockito.Mockito.*;

import com.github.StudentsDreamTeam.enums.AchievementType;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import com.github.StudentsDreamTeam.service.AchievementDetector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AchievementDetectorTests {

    private static final LocalDateTime now = LocalDateTime.now();

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AchievementDetector achievementDetector;

    private static Achievement createSampleAchievement(Integer id, AchievementType type, int value) {
        Achievement sample = new Achievement();
        sample.setName("test");
        sample.setDescription("test");
        sample.setType(type);
        sample.setRequiredValue(value);
        sample.setIcon(null);
        sample.setId(id);

        return sample;
    }

    public static User createSampleUser(Integer id) {
        User expected = new User();
        expected.setId(id);
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(0);
        return expected;
    }

    @Test
    public void detectionTest() {

        Achievement achievement = createSampleAchievement(0, AchievementType.XP, 100);

        User user = createSampleUser(0);

        when(achievementRepository.findAchievementsNotOwnedByUserNative(user.getId())).thenReturn(List.of(achievement));
        when(userAchievementRepository.save(any())).thenReturn(any());

        achievementDetector.detectForUser(user);

        verify(achievementRepository).findAchievementsNotOwnedByUserNative(user.getId());
        verifyNoMoreInteractions(achievementRepository);

        verify(userAchievementRepository).save(any());
        verifyNoMoreInteractions(userAchievementRepository);

        verifyNoInteractions(taskRepository);


    }

}
