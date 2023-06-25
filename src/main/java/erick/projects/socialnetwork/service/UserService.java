package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        user.setActive(true);
        userRepository.save(user);
    }
}
