package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Follow;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.FollowService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling HTTP requests related to following and unfollowing users.
 */
@Controller
public class FollowController {
    private final UserService userService;
    private final FollowService followService;

    /**
     * Constructor for FollowController.
     *
     * @param userService   the service for accessing users in the database
     * @param followService the service for managing follow relationships between users
     */
    public FollowController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
    }

    /**
     * Creates a follow relationship between the current user and the specified user.
     *
     * @param username the username of the user to follow
     * @param session  the HTTP session
     * @return a redirect to the home page
     */
    @PostMapping("/follow/{username}")
    public String followUser(@PathVariable("username") String username, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        User follower = userService.findByUsername(tmp.getUsername());
        User followed = userService.findByUsername(username);
        followService.follow(follower, followed);
        return "redirect:/home";
    }

    /**
     * Removes a follow relationship between the current user and the specified user.
     *
     * @param username the username of the user to unfollow
     * @param session  the HTTP session
     * @return a redirect to the home page
     */
    @PostMapping("/unfollow/{username}")
    public String unfollowUser(@PathVariable("username") String username, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        User follower = userService.findByUsername(tmp.getUsername());
        User followed = userService.findByUsername(username);
        followService.unfollow(follower, followed);
        return "redirect:/home";
    }

    /**
     * Displays a list of users that are following the specified user.
     *
     * @param username the username of the user whose followers should be displayed
     * @param model    the model for passing data to the view
     * @param session  the HTTP session
     * @param request
     * @return either "followers" or "redirect:/login" depending on whether or not there is a logged-in user
     */
    @GetMapping("{username}/followers")
    public String showFollowers(@PathVariable("username") String username, Model model, HttpSession session, HttpServletRequest request) {
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If user is logged in proceed
            User sessionUser = userService.findByUsername(tmp.getUsername());
            User user = userService.findByUsername(username);
            model.addAttribute("currentPath", request.getRequestURI());
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("user", user);
            List<User> users = new ArrayList<>();
            for (Follow follow : user.getFollowers()) {
                users.add(follow.getFollower());
            }
            model.addAttribute("users", users);
            return "users";
        } else {
            // If user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }

    /**
     * Displays a list of users that are being followed by the specified user.
     *
     * @param username the username of the user whose following should be displayed
     * @param model    the model for passing data to the view
     * @param session  the HTTP session
     * @return either "following" or "redirect:/login" depending on whether or not there is a logged-in user
     */
    @GetMapping("{username}/following")
    public String showFollowing(@PathVariable("username") String username, Model model, HttpSession session, HttpServletRequest request) {
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If user is logged in proceed
            User user = userService.findByUsername(username);
            User sessionUser = userService.findByUsername(tmp.getUsername());
            model.addAttribute("currentPath", request.getRequestURI());
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("user", user);
            List<User> users = new ArrayList<>();
            for (Follow follow : user.getFollowing()) {
                users.add(follow.getFollowed());
            }
            model.addAttribute("users", users);
            return "users";
        } else {
            // If user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }
}
