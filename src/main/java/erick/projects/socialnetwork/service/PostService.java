package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.Follow;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.FollowRepository;
import erick.projects.socialnetwork.repository.PostRepository;
import erick.projects.socialnetwork.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, FollowRepository followRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Transactional
    public void createPost(Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        post.setUser(userRepository.findByUsername(user.getUsername()));
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public List<Post> getFeedPosts(User user) {
        List<Follow> follows = followRepository.findByFollower(user);
        List<User> followedUsers = follows.stream().map(Follow::getFollowed).toList();
        List<Post> feedPosts = postRepository.findByUserIn(followedUsers);
        feedPosts.addAll(user.getPosts());
        return feedPosts;
    }

    public FollowRepository getFollowRepository() {
        return followRepository;
    }

    public PostRepository getPostRepository() {
        return postRepository;
    }
}
