package erick.projects.socialnetwork.service;

import erick.projects.socialnetwork.model.Like;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void likePost(Post post, User user) {
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
    }

    public void unlikePost(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user);
        if (like != null) {
            likeRepository.delete(like);
        }
    }

    public boolean isPostLikedByUser(Post post, User sessionUser) {
        return likeRepository.findByPostAndUser(post, sessionUser) != null;
    }

    public Like getLikeByPostAndUser(Post post, User sessionUser) {
        return likeRepository.findByPostAndUser(post, sessionUser);
    }
}