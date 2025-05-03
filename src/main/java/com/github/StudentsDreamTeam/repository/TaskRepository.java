package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @EntityGraph(attributePaths = {"author", "executor"})
    List<Task> findByAuthorIdOrExecutorId(Long authorId, Long executorId);

    List<Task> findByAuthorId(Long authorId);
    List<Task> findByExecutorId(Long executorId);
}