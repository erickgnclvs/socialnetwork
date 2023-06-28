package erick.projects.socialnetwork.repository;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIn(List<User> userList);

}
