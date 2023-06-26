package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        user.setActive(true);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public boolean authenticateUser(User user) {
        // retrieve user from database by username
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if (userFromDatabase != null) {
            // compare provided password with the one stored in the database
            return passwordEncoder.matches(user.getPassword(), userFromDatabase.getPassword());
        }
        return false;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
