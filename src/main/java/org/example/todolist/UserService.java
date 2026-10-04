package org.example.todolist;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Konta demo, żeby po pierwszym uruchomieniu dało się od razu zalogować. */
    @PostConstruct
    void createDemoUsers() {
        createIfMissing("admin", "admin", Set.of("USER", "ADMIN"));
        createIfMissing("user", "user", Set.of("USER"));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void register(String username, String rawPassword) {
        if (userExists(username)) {
            throw new IllegalArgumentException("Użytkownik o tej nazwie już istnieje");
        }
        save(username, rawPassword, Set.of("USER"));
        log.info("Registered user {}", username);
    }

    private void createIfMissing(String username, String rawPassword, Set<String> roles) {
        if (!userExists(username)) {
            save(username, rawPassword, roles);
            log.info("Created demo user {}", username);
        }
    }

    private void save(String username, String rawPassword, Set<String> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRoles(roles);
        userRepository.save(user);
    }
}
