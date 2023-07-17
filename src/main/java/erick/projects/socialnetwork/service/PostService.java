package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.Follow;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.FollowRepository;
import erick.projects.socialnetwork.repository.LikeRepository;
import erick.projects.socialnetwork.repository.PostRepository;
import erick.projects.socialnetwork.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * A service class for managing posts related actions.
 */
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;

    /**
     * Constructor with @Autowired annotation to automatically inject dependencies.
     *
     * @param postRepository   the PostRepository to use
     * @param userRepository   the UserRepository to use
     * @param followRepository the FollowRepository to use
     * @param likeRepository   the LikeRepository to use
     */
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, FollowRepository followRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.likeRepository = likeRepository;
    }

    /**
     * Creates a new post from a user.
     *
     * @param post    the post to create
     * @param session the HTTP session
     */
    @Transactional
    public void createPost(Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        post.setUser(userRepository.findByUsername(user.getUsername()));
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Transactional
    public Post createPostReturnPost(Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        post.setUser(userRepository.findByUsername(user.getUsername()));
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }


    /**
     * Returns a list of posts for a user's feed.
     *
     * @param user the user whose feed to retrieve
     * @return a list of posts for the user's feed
     */
    public List<Post> getFeedPosts(User user) {
        // Find all follow relationships where the user is the follower
        List<Follow> follows = followRepository.findByFollower(user);
        // Extract a list of followed users from the follow relationships
        List<User> followedUsers = follows.stream().map(Follow::getFollowed).toList();
        // Find all posts by followed users using the postRepository
        List<Post> feedPosts = postRepository.findByUserIn(followedUsers);
        // Add all posts by the user to the list of feed posts
        feedPosts.addAll(user.getPosts());
        // Sort the feed posts in reverse chronological order by creation time
        feedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
        // Return the list of feed posts
        return feedPosts;
    }

    /**
     * Deletes a post by its id.
     *
     * @param postId the id of the post to delete
     */
    public void deletePostById(Long postId) {
        Post post = getPostById(postId);
        postRepository.delete(post);
    }

    /**
     * Returns a post by its id.
     *
     * @param postId the id of the post to retrieve
     * @return the post with the given id, or null if it doesn't exist
     */
    public Post getPostById(Long postId) {
        return postRepository.getPostById(postId);
    }

    public LikeRepository getLikeRepository() {
        return likeRepository;
    }

    public FollowRepository getFollowRepository() {
        return followRepository;
    }


    public PostRepository getPostRepository() {
        return postRepository;
    }
}
