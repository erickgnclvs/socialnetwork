package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.LikeService;
import erick.projects.socialnetwork.service.PostService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LikeController {
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;

    public LikeController(UserService userService, PostService postService, LikeService likeService) {
        this.userService = userService;
        this.postService = postService;
        this.likeService = likeService;
    }

    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") Long postId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        User user = userService.findByUsername(sessionUser.getUsername());
        Post post = postService.getPostById(postId);
        likeService.likePost(post, user);
        return "redirect:/post/{postId}";
    }

    public UserService getUserService() {
        return userService;
    }

    public PostService getPostService() {
        return postService;
    }

    public LikeService getLikeService() {
        return likeService;
    }
}
