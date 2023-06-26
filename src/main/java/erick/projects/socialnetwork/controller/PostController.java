package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.PostService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    public PostService getPostService() {
        return postService;
    }

    @GetMapping("/create-post")
    public String showCreatePostForm(Model model, HttpSession session) {
        // check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // if user is logged in, add any necessary data to the model
            model.addAttribute("post", new Post());
            return "create_post";
        } else {
            // if user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("post") Post post, HttpSession session) {
        postService.createPost(post, session);
        return "redirect:/home";
    }

    public UserService getUserService() {
        return userService;
    }
}
