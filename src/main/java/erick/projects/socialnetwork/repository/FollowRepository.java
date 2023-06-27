package erick.projects.socialnetwork.repository;

import erick.projects.socialnetwork.model.Follow;
import erick.projects.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);

    Follow findByFollowerAndFollowed(User follower, User followed);

    boolean existsByFollowerAndFollowed(User follower, User followed);
}
