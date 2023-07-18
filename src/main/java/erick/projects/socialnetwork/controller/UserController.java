package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Image;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.ImageRepository;
import erick.projects.socialnetwork.service.FollowService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Controller for handling user-related HTTP requests.
 */
@Controller
public class UserController {
    // Fields for the FollowService, UserService, and ImageRepository
    private final FollowService followService;
    private final UserService userService;
    private final ImageRepository imageRepository;

    /**
     * Constructor with @Autowired annotation to automatically inject dependencies.
     *
     * @param followService   the FollowService to use
     * @param userService     the UserService to use
     * @param imageRepository the ImageRepository to use
     */
    @Autowired
    public UserController(FollowService followService, UserService userService, ImageRepository imageRepository) {
        this.followService = followService;
        this.userService = userService;
        this.imageRepository = imageRepository;
    }

    /**
     * Shows registration form.
     *
     * @param model the model to add attributes to
     * @return the view name "register"
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Add a new User object to the model
        model.addAttribute("user", new User());
        // Return the view name "register"
        return "register";
    }

    /**
     * Registers user in database.
     *
     * @param user the user object created from the form data
     * @return a redirect to the /login endpoint
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Validate user input (e.g., check if username is taken)
        boolean isUsernameTaken = userService.isUsernameTaken(user.getUsername());
        boolean isEmailTaken = userService.isEmailTaken(user.getEmail());
        if (isUsernameTaken) {
            // If the username is taken, add an error message to the model and return the view name "register"
            model.addAttribute("error", "Username is already taken");
            user.setUsername(null);
            model.addAttribute("registrationFailed", true);
            return "register";
        } else if (isEmailTaken) {
            // If the username is taken, add an error message to the model and return the view name "register"
            model.addAttribute("error", "Email is already taken");
            user.setEmail(null);
            model.addAttribute("registrationFailed", true);
            return "register";
        } else {
            // If the username is not taken, create a new user using the userService
            userService.createUser(user);
            // Redirect to the /login endpoint
            return "redirect:/login";
        }
    }


    /**
     * Displays login form.
     *
     * @param model   the model to add attributes to
     * @param session the HTTP session
     * @return either a redirect to the /home endpoint or the view name "login"
     */
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        // Check if there is a user object in the session
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If there is, redirect to the /home endpoint
            return "redirect:/home";
        } else {
            // Otherwise, add a new User object to the model
            model.addAttribute("user", new User());
            // Return the view name "login"
            return "login";
        }
    }

    /**
     * Logs user in.
     *
     * @param user    the user object created from the form data
     * @param model   the model to add attributes to
     * @param session the HTTP session
     * @return either a redirect to the /home endpoint or the view name "login"
     */
    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
        // Check if there is a user object in the session
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If there is, redirect to the /home endpoint
            return "redirect:/home";
        }
        // Authenticate the user using the userService
        boolean isAuthenticated = userService.authenticateUser(user);
        if (isAuthenticated) {
            // If authentication is successful, save the user object to the session and redirect to the /home endpoint
            session.setAttribute("user", user);
            return "redirect:/home";
        } else {
            // If authentication fails, add an error message to the model and return the view name "login"
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("authFailed", true);
            return "login";
        }
    }

    /**
     * Logs user out.
     *
     * @param session the HTTP session
     * @return a redirect to the /login endpoint
     */
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        // Invalidate the session and redirect to the /login endpoint
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            session.invalidate();
            // Redirect to login page
        }
        return "redirect:/login";
    }

    /**
     * Displays user's profile which username is in the url.
     *
     * @param username the username of the user being accessed
     * @param model    the model to add attributes to
     * @param session  the HTTP session
     * @param request
     * @return either a redirect or a view name depending on the situation
     */
    @GetMapping("/{username}")
    public String showUserProfile(@PathVariable String username, Model model, HttpSession session, HttpServletRequest request) {
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
                List<Post> posts = user.getPosts();
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                model.addAttribute("currentPath", request.getRequestURI());
                model.addAttribute("posts", posts);
                boolean isFollowing = followService.isFollowing(sessionUser, user);
                model.addAttribute("isFollowing", isFollowing);
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

    /**
     * Displays list with all users.
     *
     * @param model   the model to add attributes to
     * @param session the HTTP session
     * @param request
     * @return either the view name "users" or a redirect to the /login endpoint
     */
    @GetMapping("/users")
    public String showAllUsers(Model model, HttpSession session, HttpServletRequest request) {
        // Check if there is a user object in the session
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If there is, retrieve all users from the database using the userService and add them to the model along with the current user
            List<User> allUsers = userService.findAll();
            User sessionUser = userService.findByUsername(tmp.getUsername());
            model.addAttribute("currentPath", request.getRequestURI());
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("users", allUsers);
            // Return the view name "users"
            return "users";
        }
        // If there is no user object in the session, redirect to the /login endpoint
        return "redirect:/login";
    }

    /**
     * Displays a form to edit user's information.
     *
     * @param model   the model to add attributes to
     * @param session the HTTP session
     * @return either the view name "edit_profile" or a redirect to the /login endpoint
     */
    @GetMapping("/profile/edit")
    public String showEditProfile(Model model, HttpSession session) {
        // Check if there is a user object in the session
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If there is, retrieve the current user from the database using the userService and add it to the model along with a copy of itself
            User sessionUser = userService.findByUsername(tmp.getUsername());
            User user = userService.findByUsername(tmp.getUsername());
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("user", user);
            // Return the view name "edit_profile"
            return "edit_profile";
        }
        // If there is no user object in the session, redirect to the /login endpoint
        return "redirect:/login";
    }

    /**
     * Edits user.
     *
     * @param name           the new name of the user
     * @param username       the new username of the user
     * @param email          the new email of the user
     * @param biography      the new biography of the user
     * @param profilePicture the new profile picture of the user
     * @param session        the HTTP session
     * @return either a redirect to the /home endpoint or a redirect to the /login endpoint
     */
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam("name") String name,
                              @RequestParam("username") String username,
                              @RequestParam("email") String email,
                              @RequestParam("biography") String biography,
                              @RequestParam("profilePicture") MultipartFile profilePicture,
                              HttpSession session) {
        // Retrieve the current user from the session
        User tmp = (User) session.getAttribute("user");
        if (tmp != null) {
            // If there is a user object in the session, retrieve it from the database using the userService
            User user = userService.findByUsername(tmp.getUsername());

            // Update its fields with values from form data
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            user.setBiography(biography);

            // Check if a new profile picture was uploaded
            if (!profilePicture.isEmpty()) {
                try {
                    // Create a new Image object and set its fields with values from form data
                    Image img = new Image();
                    img.setImageName(profilePicture.getOriginalFilename());
                    img.setImageType(profilePicture.getContentType());
                    img.setImage(profilePicture.getBytes());

                    // Save it to database using imageRepository and set it as profile picture for current user
                    imageRepository.save(img);
                    user.setProfileImage(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Update current user in database using userService and redirect to home page
            userService.save(user);
            return "redirect:/home";
        } else {
            // If there is no user object in session, redirect to login page
            return "redirect:/login";
        }
    }
}
