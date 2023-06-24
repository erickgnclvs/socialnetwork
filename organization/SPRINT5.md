# Sprint 5: Implementing likes

**Sprint 5: Implementing likes**
- Create a `Like` model class to represent a like on a post. This class should have fields for the post that was liked and the user who liked it. It should also be annotated with `@Entity` to indicate that it's a JPA entity.
```java
@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Post post;
    @ManyToOne
    private User user;

    // getters and setters
}
```
- Create a `LikeRepository` interface that extends `JpaRepository`. This interface provides methods for performing CRUD operations on `Like` objects.
```java
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPost(Post post);
}
```
- Create a `LikeService` class that provides methods for interacting with `Like` objects. This class should use the `LikeRepository` to perform database operations.
```java
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void likePost(Post post, User user) {
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
    }

    public void unlikePost(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user);
        if (like != null) {
            likeRepository.delete(like);
        }
    }
}
```
- Create a `LikeController` class that handles HTTP requests related to liking/unliking posts. This class should use the `LikeService` to perform business logic.
```java
@Controller
public class LikeController {
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;

    public LikeController(UserService userService, PostService postService, LikeService likeService) {
        this.userService = userService;
        this.postService = postService;
        this.likeService = likeService;
    }

    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") Long postId,
                           Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Post post = postService.getPostById(postId);
        likeService.likePost(post, user);
        return "redirect:/home";
    }

    @PostMapping("/unlike/{postId}")
    public String unlikePost(@PathVariable("postId") Long postId,
                             Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Post post = postService.getPostById(postId);
        likeService.unlikePost(post, user);
        return "redirect:/home";
    }
}
```
- In your JSP pages (e.g., on the home page), add buttons for liking/unliking posts. These buttons should submit forms to the server to either like or unlike the post.
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>Home</h1>
<c:forEach items="${feedPosts}" var="post">
    <h2>${post.user.username}</h2>
    <p>${post.content}</p>
    <form method="POST" action="/like/${post.id}">
        <input type="submit" value="Like"/>
    </form>
</c:forEach>
</body>
</html>
```
- In the `LikeController`, use the `LikeService` to either create or delete a `Like` object when a user likes or unlikes a post.
