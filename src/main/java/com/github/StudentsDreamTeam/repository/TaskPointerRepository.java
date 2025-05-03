package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.TaskPointer;
import com.github.StudentsDreamTeam.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPointerRepository extends JpaRepository<TaskPointer, Integer> {
    void deleteAllByLinkedTask(Task linkedTask);

}
