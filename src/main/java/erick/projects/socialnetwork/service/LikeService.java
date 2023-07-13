package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.Like;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.LikeRepository;
import org.springframework.stereotype.Service;

/**
 * A service class for managing likes on posts.
 */
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    /**
     * Constructor that takes a LikeRepository as an argument and initializes the likeRepository field.
     *
     * @param likeRepository the LikeRepository to use
     */
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    /**
     * Adds a like to a post from a user.
     *
     * @param post the post to like
     * @param user the user liking the post
     */
    public void likePost(Post post, User user) {
        Like like = new Like();
        if (likeRepository.findByPostAndUser(post, user) == null) {
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }

    }

    /**
     * Removes a like from a post by a user.
     *
     * @param post the post to unlike
     * @param user the user unliking the post
     */
    public void unlikePost(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user);
        if (like != null) {
            likeRepository.delete(like);
        }
    }

    /**
     * Checks if a post is liked by a user.
     *
     * @param post        the post to check
     * @param sessionUser the user to check
     * @return true if the post is liked by the user, false otherwise
     */
    public boolean isPostLikedByUser(Post post, User sessionUser) {
        return likeRepository.findByPostAndUser(post, sessionUser) != null;
    }

    /**
     * Returns the Like object representing a relationship between a post and a user.
     *
     * @param post        the post to check
     * @param sessionUser the user to check
     * @return the Like object representing the relationship, or null if it doesn't exist
     */
    public Like getLikeByPostAndUser(Post post, User sessionUser) {
        return likeRepository.findByPostAndUser(post, sessionUser);
    }
}
