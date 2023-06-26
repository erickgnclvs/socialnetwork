package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.Follow;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.FollowRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public FollowRepository getFollowRepository() {
        return followRepository;
    }

    public void follow(User follower, User followed) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        followRepository.save(follow);
    }
}
