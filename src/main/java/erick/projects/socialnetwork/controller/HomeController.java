package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.PostService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final PostService postService;

    public HomeController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/")
    public String redirectHomePage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHomePage(Model model, HttpSession session) {
        // check if user is logged in
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            // if user is logged in, add any necessary data to the model
            // ...
            User user = userService.findByUsername(sessionUser.getUsername());
            List<Post> feedPosts = postService.getFeedPosts(user);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, MMMM d, yyyy");
            model.addAttribute("formatter", formatter);
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("feedPosts", feedPosts);
            return "home";
        } else {
            // if user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }

}
