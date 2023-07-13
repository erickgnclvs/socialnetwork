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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling HTTP requests related to liking and unliking posts.
 */
@Controller
public class LikeController {
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;

    /**
     * Constructor for LikeController.
     *
     * @param userService the service for accessing users in the database
     * @param postService the service for accessing posts in the database
     * @param likeService the service for managing likes on posts
     */
    public LikeController(UserService userService, PostService postService, LikeService likeService) {
        this.userService = userService;
        this.postService = postService;
        this.likeService = likeService;
    }

    /**
     * Likes post
     */
    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") Long postId, HttpSession session, @RequestParam("referer") String referer) {
        User sessionUser = (User) session.getAttribute("user");
        User user = userService.findByUsername(sessionUser.getUsername());
        Post post = postService.getPostById(postId);
        likeService.likePost(post, user);
        return "redirect:" + referer;
    }

    /**
     * Unlikes post
     */
    @PostMapping("/unlike/{postId}")
    public String unlikePost(@PathVariable("postId") Long postId, HttpSession session, @RequestParam("referer") String referer) {
        User sessionUser = (User) session.getAttribute("user");
        User user = userService.findByUsername(sessionUser.getUsername());
        Post post = postService.getPostById(postId);
        likeService.unlikePost(post, user);
        return "redirect:" + referer;
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
