package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @EntityGraph()
    List<Task> findByUserId(Long userId);
}
