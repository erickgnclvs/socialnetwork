package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.Follow;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.FollowRepository;
import org.springframework.stereotype.Service;

/**
 * A service class for managing follow relationships between users.
 */
@Service
public class FollowService {
    private final FollowRepository followRepository;

    /**
     * Constructor that takes a FollowRepository as an argument and initializes the followRepository field.
     *
     * @param followRepository the FollowRepository to use
     */
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    /**
     * Returns the FollowRepository used by this service.
     *
     * @return the FollowRepository used by this service
     */
    public FollowRepository getFollowRepository() {
        return followRepository;
    }

    /**
     * Creates a new follow relationship between two users.
     *
     * @param follower the follower in the relationship
     * @param followed the followed user in the relationship
     */
    public void follow(User follower, User followed) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        followRepository.save(follow);
    }

    /**
     * Deletes an existing follow relationship between two users.
     *
     * @param follower the follower in the relationship
     * @param followed the followed user in the relationship
     */
    public void unfollow(User follower, User followed) {
        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    /**
     * Checks if a user is following another user.
     *
     * @param follower the follower in the relationship
     * @param followed the followed user in the relationship
     * @return true if a follow relationship exists, false otherwise
     */
    public boolean isFollowing(User follower, User followed) {
        return followRepository.existsByFollowerAndFollowed(follower, followed);
    }
}
