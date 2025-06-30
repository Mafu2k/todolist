package org.example.todolist;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Task {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean done = false;
    
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    
    // konstruktor domyslny
    public Task() {
        this.createdAt = LocalDateTime.now();
    }
    
    // konstruktor z parametrami
    public Task(String title, String description, User user) {
        this();
        this.title = title;
        this.description = description;
        this.user = user;
    }

    // gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
        // ustawiam date zakonczenia
        if (done) {
            this.completedAt = LocalDateTime.now();
        } else {
            this.completedAt = null;
        }
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    // sprawdzam czy zadanie jest przeterminowane
    public boolean isOverdue() {
        if (dueDate == null || done) {
            return false;
        }
        return dueDate.isBefore(LocalDate.now());
    }
}
