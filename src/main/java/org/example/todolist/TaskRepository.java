package org.example.todolist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Podstawowe zapytania
    List<Task> findByUser(User user);
    List<Task> findByUserOrderByCreatedAtDesc(User user);
    List<Task> findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(User user, String title);
    
    // Statystyki
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user")
    long countByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.done = true")
    long countCompletedByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.done = false")
    long countPendingByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.dueDate < :date AND t.done = false")
    long countOverdueByUser(@Param("user") User user, @Param("date") LocalDate date);
}
