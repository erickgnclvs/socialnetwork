package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.FollowService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Controller
public class UserController {
    private final FollowService followService;
    private final UserService userService;

    @Autowired
    public UserController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // validate user input (e.g., check if username is taken)
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            return "redirect:/home";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            System.out.println("user is not null");
            return "redirect:/home";
        }
        boolean isAuthenticated = userService.authenticateUser(user);
        if (isAuthenticated) {
            // if authentication is successful, redirect to home page
            // save login to session
            session.setAttribute("user", user);
            return "redirect:/home";
        } else {
            // if authentication fails, add error message and return to login page
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        // invalidate session
        // check if user is logged in
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            session.invalidate();
            // redirect to login page
        }
        return "redirect:/login";
        // if user is logged in...

    }

    @GetMapping("/{username}")
    public String showUserProfile(@PathVariable String username, Model model, HttpSession session) {
        // check if user is logged in
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // if user is logged in...
            // retrieve user from database by username
            User sessionUser = userService.findByUsername(tmp.getUsername());
            model.addAttribute("sessionUser", sessionUser);
            User user = userService.findByUsername(username);
            if (user != null) {
                // if user is found, add it to the model
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, MMMM d, yyyy");
                model.addAttribute("formatter", formatter);
                model.addAttribute("user", user);
                List<Post> posts = user.getPosts();
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                model.addAttribute("posts", posts);
                // check if session user is following accessed user
                boolean isFollowing = followService.isFollowing(sessionUser, user);
                model.addAttribute("isFollowing", isFollowing);
                return "profile";
            } else {
                // if user is not found, redirect to error page or show error message
                return "redirect:/error";
            }
        } else {
            // if user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }

    @GetMapping("/users")
    public String showAllUsers(Model model, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            List<User> allUsers = userService.findAll();
            model.addAttribute("sessionUser", tmp);
            model.addAttribute("users", allUsers);
            return "users";
        }
        return "redirect:/login";

    }

    @GetMapping("/profile/edit")
    public String showEditProfile(Model model, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            User sessionUser = userService.findByUsername(tmp.getUsername());
            User user = userService.findByUsername(tmp.getUsername());
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("user", user);
            return "edit_profile";
        }
        return "redirect:/login";
    }
}
