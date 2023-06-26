package erick.projects.socialnetwork.repository;

import erick.projects.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
