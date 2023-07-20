package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.PostService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for handling HTTP requests related to the home page.
 */
@Controller
public class HomeController {
    private final UserService userService;
    private final PostService postService;

    /**
     * Constructor for HomeController.
     *
     * @param userService the service for accessing users in the database
     * @param postService the service for accessing posts in the database
     */
    public HomeController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    /**
     * Redirects the user to the home page.
     *
     * @return a redirect to /home
     */
    @GetMapping("/")
    public String redirectHomePage() {
        return "redirect:/home";
    }

    /**
     * Displays the home page with a feed of posts from users that the current user is following.
     *
     * @param model   the model for passing data to the view
     * @param session the HTTP session
     * @return either "home" or "redirect:/login" depending on whether or not there is a logged-in user
     */
    @GetMapping("/home")
    public String showHomePage(Model model, HttpSession session, HttpServletRequest request) {
        // Check if user is logged in
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If user is logged in proceed
            User sessionUser = userService.findByUsername(tmp.getUsername());
            List<Post> feedPosts = postService.getFeedPosts(sessionUser);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, MMMM d, yyyy");
            model.addAttribute("formatter", formatter);
            DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("K:mma");
            model.addAttribute("currentPath", request.getRequestURI());
            model.addAttribute("formatterHour", formatterHour);
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("user", sessionUser);
            model.addAttribute("feedPosts", feedPosts);
            return "home";
        } else {
            // If user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }
}
