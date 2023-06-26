package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String showHomePage(Model model, HttpSession session) {
        // check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // if user is logged in, add any necessary data to the model
            // ...
            return "home";
        } else {
            // if user is not logged in, redirect to login page
            return "redirect:/login";
        }
    }

}
