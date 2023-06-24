# Sprint 3: Posting and viewing content

**Step 1: Implement posting text-based content**
- Create a `Post` model class to represent a post in your app. This class should have fields for the post's content and the user who created it. It should also be annotated with `@Entity` to indicate that it's a JPA entity.
```java
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private User user;

    // getters and setters
}
```
- Create a `PostRepository` interface that extends `JpaRepository`. This interface provides methods for performing CRUD operations on `Post` objects.
```java
public interface PostRepository extends JpaRepository<Post, Long> {
}
```
- Create a `PostService` class that provides methods for interacting with `Post` objects. This class should use the `PostRepository` to perform database operations.
```java
@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }
}
```
- Create a `PostController` class that handles HTTP requests related to posts. This class should use the `PostService` to perform business logic.
```java
@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/create-post")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create-post";
    }

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("post") Post post,
                             Principal principal) {
        // set the user of the post (e.g., using the Principal object)
        // validate post input (e.g., check if content is not empty)
        postService.savePost(post);
        return "redirect:/home";
    }
}
```
- Create a form for creating posts in JSP. This form should allow users to enter their post content and submit it to the server.
```jsp
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Create Post</title>
</head>
<body>
<h1>Create Post</h1>
<form:form method="POST" action="/create-post" modelAttribute="post">
    <table>
        <tr>
            <td>Content:</td>
            <td><form:textarea path="content"/></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="Create"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>
```
- In the `PostController`, set the user of the post (e.g., using the `Principal` object), validate the post input (e.g., check if the content is not empty), and use the `PostService` to save the post to the database.

**Step 2: Implement viewing a feed of posts from followed users**
- In the `PostService`, create a method for retrieving posts from followed users. This method should use the `PostRepository` to perform a database query.
```java
@Service
public class PostService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    public PostService(PostRepository postRepository, FollowRepository followRepository) {
        this.postRepository = postRepository;
        this.followRepository = followRepository;
    }

    // ...

    public List<Post> getFeedPosts(User user) {
        List<Follow> follows = followRepository.findByFollower(user);
        List<User> followedUsers = follows.stream()
                .map(Follow::getFollowed)
                .collect(Collectors.toList());
        return postRepository.findByUserIn(followedUsers);
    }
}
```
- In the `HomeController`, create a method for displaying the home page. This method should retrieve the feed posts using the `PostService` and add them to the model.
```java
@Controller
public class HomeController {
    private final UserService userService;
    private final PostService postService;

    public HomeController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        List<Post> feedPosts = postService.getFeedPosts(user);
        model.addAttribute("feedPosts", feedPosts);
        return "home";
    }
}
```
- Create a JSP page for the home page. This page should display the feed posts using Bootstrap components (e.g., cards).
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1>Home</h1>
    <div class="row">
        <c:forEach items="${feedPosts}" var="post">
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${post.user.username}</h5>
                        <p class="card-text">${post.content}</p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
```