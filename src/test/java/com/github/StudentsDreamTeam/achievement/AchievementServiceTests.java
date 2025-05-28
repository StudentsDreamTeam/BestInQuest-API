package com.github.StudentsDreamTeam.achievement;

import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import com.github.StudentsDreamTeam.service.AchievementDetector;
import com.github.StudentsDreamTeam.service.AchievementService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class AchievementServiceTests {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementDetector detector;

    @InjectMocks
    private AchievementService achievementService;
}
