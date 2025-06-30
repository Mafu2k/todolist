package org.example.todolist;

import org.example.todolist.User;
import org.example.todolist.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializeTestUser() {
        System.out.println("Initializing test users...");
        try {
            // Tworzymy testowego użytkownika jeśli nie istnieje
            if (userRepository.findByUsername("admin").isEmpty()) {
                User testUser = new User();
                testUser.setUsername("admin");
                testUser.setPasswordHash(passwordEncoder.encode("admin"));
                testUser.setRoles(Set.of("USER", "ADMIN"));
                userRepository.save(testUser);
                System.out.println("Created admin user");
            } else {
                System.out.println("Admin user already exists");
            }
            
            if (userRepository.findByUsername("user").isEmpty()) {
                User testUser = new User();
                testUser.setUsername("user");
                testUser.setPasswordHash(passwordEncoder.encode("user"));
                testUser.setRoles(Set.of("USER"));
                userRepository.save(testUser);
                System.out.println("Created user user");
            } else {
                System.out.println("User user already exists");
            }
        } catch (Exception e) {
            System.out.println("Error initializing users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    public void register(String username, String rawPassword) {
        if (userExists(username)) {
            throw new RuntimeException("Użytkownik o tej nazwie już istnieje");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of("USER"));
        userRepository.save(user);
        System.out.println("Successfully registered user: " + username);
    }
}
