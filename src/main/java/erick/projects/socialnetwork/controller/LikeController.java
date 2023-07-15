package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Like;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.FollowService;
import erick.projects.socialnetwork.service.LikeService;
import erick.projects.socialnetwork.service.PostService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Controller for handling HTTP requests related to liking and unliking posts.
 */
@Controller
public class LikeController {
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;
    private final FollowService followService;

    /**
     * Constructor for LikeController.
     *
     * @param userService   the service for accessing users in the database
     * @param postService   the service for accessing posts in the database
     * @param likeService   the service for managing likes on posts
     * @param followService the service for managing follows
     */
    public LikeController(UserService userService, PostService postService, LikeService likeService, FollowService followService) {
        this.userService = userService;
        this.postService = postService;
        this.likeService = likeService;
        this.followService = followService;
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

    @GetMapping("/{username}/likes")
    public String showLikedPosts(@PathVariable("username") String username, Model model, HttpSession session, HttpServletRequest request) {
        // Check if there is a user object in the session
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If there is, retrieve a user from the database using the userService and add it to the model along with other relevant information
            User sessionUser = userService.findByUsername(tmp.getUsername());
            model.addAttribute("sessionUser", sessionUser);
            User user = userService.findByUsername(username);
            if (user != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, MMMM d, yyyy");
                DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("K:mma");
                model.addAttribute("formatterHour", formatterHour);
                model.addAttribute("formatter", formatter);
                model.addAttribute("user", user);
                List<Like> likes = user.getLikes();
                List<Post> posts = new ArrayList<>();
                for (Like like : likes) {
                    posts.add(like.getPost());
                }
                model.addAttribute("currentPath", request.getRequestURI());
                model.addAttribute("posts", posts);
                boolean isFollowing = followService.isFollowing(sessionUser, user);
                model.addAttribute("isFollowing", isFollowing);
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                return "profile";
            } else {
                // If no user is found, redirect to an error page or show an error message
                return "redirect:/error";
            }
        } else {
            // If there is no user object in the session, redirect to the /login endpoint
            return "redirect:/login";
        }

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
