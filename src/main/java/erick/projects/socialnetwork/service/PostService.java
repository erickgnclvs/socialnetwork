package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.PostRepository;
import erick.projects.socialnetwork.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public PostRepository getPostRepository() {
        return postRepository;
    }

    @Transactional
    public void createPost(Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        post.setUser(userRepository.findByUsername(user.getUsername()));
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
