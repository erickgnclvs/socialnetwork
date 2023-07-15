package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service class for managing user accounts.
 */
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Constructor with @Autowired annotation to automatically inject dependencies.
     *
     * @param passwordEncoder the PasswordEncoder to use
     * @param userRepository  the UserRepository to use
     */
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user account.
     *
     * @param user the user to create
     */
    public void createUser(User user) {
        user.setActive(true);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    /**
     * Authenticates a user.
     *
     * @param user the user to authenticate
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(User user) {
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if (userFromDatabase != null) {
            return passwordEncoder.matches(user.getPassword(), userFromDatabase.getPassword());
        }
        return false;
    }

    /**
     * Returns a user by its username.
     *
     * @param username the username of the user to retrieve
     * @return the user with the given username, or null if it doesn't exist
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Returns a list of all users.
     *
     * @return a list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user the user to update
     */
    public void save(User user) {
        userRepository.save(user);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
