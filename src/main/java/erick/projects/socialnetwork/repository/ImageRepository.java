package erick.projects.socialnetwork.repository;

import erick.projects.socialnetwork.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByImageName(String imageName);
}
