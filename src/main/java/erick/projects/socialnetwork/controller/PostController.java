package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.LikeService;
import erick.projects.socialnetwork.service.PostService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;

/**
 * Controller for handling HTTP requests related to creating, deleting, and viewing posts.
 */
@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;

    /**
     * Constructor for PostController.
     *
     * @param postService the service for accessing posts in the database
     * @param userService the service for accessing users in the database
     * @param likeService the service for managing likes on posts
     */
    @Autowired
    public PostController(PostService postService, UserService userService, LikeService likeService) {
        this.postService = postService;
        this.userService = userService;
        this.likeService = likeService;
    }

    /**
     * Saves a new post to the database and redirects the user to the home page.
     *
     * @param post    the new post to save
     * @param session the HTTP session
     * @return a redirect to /home
     */
    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("post") Post post, HttpSession session) {
        postService.createPost(post, session);
        return "redirect:/home";
    }

    /**
     * Deletes a post from the database if it belongs to the current user.
     *
     * @param postId  the ID of the post to delete
     * @param session the HTTP session
     * @return either "redirect:/home" or "redirect:/error" depending on whether the current user is allowed to delete this post
     */
    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Post post = postService.getPostById(postId);
        if (post.getUser().getUsername().equals(user.getUsername())) {
            postService.deletePostById(postId);
            return "redirect:/home";
        } else {
            return "redirect:/error";
        }
    }

    /**
     * Displays a single post by its ID.
     *
     * @param postId  the ID of the post to display
     * @param model   the model for passing data to the view
     * @param session the HTTP session
     * @param request
     * @return either "post" or "redirect:/login" depending on whether there is a logged-in user
     */
    @GetMapping("post/{postId}")
    public String showPostById(@PathVariable("postId") Long postId, Model model, HttpSession session, HttpServletRequest request) {
        // Check if user is logged in
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If user is logged in, proceed
            User sessionUser = userService.findByUsername(tmp.getUsername());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, MMMM d, yyyy");
            Post post = postService.getPostById(postId);
            DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("K:mma");
            model.addAttribute("currentPath", request.getRequestURI());
            model.addAttribute("formatterHour", formatterHour);
            model.addAttribute("formatter", formatter);
            model.addAttribute("post", post);
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("user", post.getUser());
            return "post";
        } else {
            // If user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public LikeService getLikeService() {
        return likeService;
    }
}
