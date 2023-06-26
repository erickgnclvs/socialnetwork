package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.service.FollowService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {
    private final UserService userService;
    private final FollowService followService;

    public FollowController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
    }

    public FollowService getFollowService() {
        return followService;
    }

    public UserService getUserService() {
        return userService;
    }

    @PostMapping("/follow/{username}")
    public String followUser(@PathVariable("username") String username, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        User follower = userService.findByUsername(tmp.getUsername());
        User followed = userService.findByUsername(username);
        followService.follow(follower, followed);
        return "redirect:/home";
    }

    @PostMapping("/unfollow/{username}")
    public String unfollowUser(@PathVariable("username") String username, HttpSession session) {
        User tmp = (User) session.getAttribute("user");
        User follower = userService.findByUsername(tmp.getUsername());
        User followed = userService.findByUsername(username);
        followService.unfollow(follower, followed);
        return "redirect:/home";
    }
}
