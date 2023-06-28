package erick.projects.socialnetwork.repository;

import erick.projects.socialnetwork.model.Like;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPost(Post post);

    Like findByPostAndUser(Post post, User user);
}