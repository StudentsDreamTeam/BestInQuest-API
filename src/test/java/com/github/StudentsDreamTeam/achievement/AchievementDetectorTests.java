package com.github.StudentsDreamTeam.achievement;

import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import com.github.StudentsDreamTeam.service.AchievementDetector;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class AchievementDetectorTests {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AchievementDetector achievementDetector;


}
