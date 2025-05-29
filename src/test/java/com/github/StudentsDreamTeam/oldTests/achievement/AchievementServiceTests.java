package com.github.StudentsDreamTeam.achievement;

import static org.mockito.Mockito.*;

import com.github.StudentsDreamTeam.dto.AchievementDTO;
import com.github.StudentsDreamTeam.enums.AchievementType;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import com.github.StudentsDreamTeam.service.AchievementDetector;
import com.github.StudentsDreamTeam.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTests {

    private static final LocalDateTime now = LocalDateTime.now();

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementDetector detector;

    @InjectMocks
    private AchievementService achievementService;

    private static Achievement createSampleAchievement(Integer id, AchievementType type, int value) {
        Achievement sample = new Achievement();
        sample.setId(id);
        sample.setName("test");
        sample.setDescription("test");
        sample.setType(type);
        sample.setRequiredValue(value);
        sample.setIcon(null);

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
    public void creationTest() {

        Achievement achievement = createSampleAchievement(0, AchievementType.XP, 100);

        when(achievementRepository.existsByName(achievement.getName())).thenReturn(false);
        when(achievementRepository.save(any())).thenReturn(achievement);

        achievementService.create(AchievementDTO.fromORM(achievement));

        verify(achievementRepository).existsByName(achievement.getName());
        verify(achievementRepository).save(any());
        verifyNoMoreInteractions(achievementRepository);


    }

}
