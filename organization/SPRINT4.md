# Sprint 4: Following and unfollowing users

**Step 1: Implement following/unfollowing other users**
- Create a `Follow` model class to represent the relationship between two users (i.e., one user following another user). This class should have fields for the follower and the followed user and should be annotated with `@Entity` to indicate that it's a JPA entity.
```java
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User follower;
    @ManyToOne
    private User followed;

    // getters and setters
}
```
- Create a `FollowRepository` interface that extends `JpaRepository`. This interface provides methods for performing CRUD operations on `Follow` objects.
```java
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
}
```
- Create a `FollowService` class that provides methods for interacting with `Follow` objects. This class should use the `FollowRepository` to perform database operations.
```java
@Service
public class FollowService {
    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void follow(User follower, User followed) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        followRepository.save(follow);
    }

    public void unfollow(User follower, User followed) {
        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }
}
```
- Create a `FollowController` class that handles HTTP requests related to following/unfollowing users. This class should use the `FollowService` to perform business logic.
```java
@Controller
public class FollowController {
    private final UserService userService;
    private final FollowService followService;

    public FollowController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
    }

    @PostMapping("/follow/{username}")
    public String followUser(@PathVariable("username") String username,
                             Principal principal) {
        User follower = userService.loadUserByUsername(principal.getName());
        User followed = userService.loadUserByUsername(username);
        followService.follow(follower, followed);
        return "redirect:/users";
    }

    @PostMapping("/unfollow/{username}")
    public String unfollowUser(@PathVariable("username") String username,
                               Principal principal) {
        User follower = userService.loadUserByUsername(principal.getName());
        User followed = userService.loadUserByUsername(username);
        followService.unfollow(follower, followed);
        return "redirect:/users";
    }
}
```
- In your JSP pages (e.g., on the user profile page), add a button for following/unfollowing users. This button should submit a form to the server to either follow or unfollow the user.
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
<h1>${user.username}</h1>
<form method="POST" action="/follow/${user.username}">
    <input type="submit" value="Follow"/>
</form>
</body>
</html>
```
- In the `FollowController`, use the `FollowService` to either create or delete a `Follow` object when a user follows or unfollows another user.
