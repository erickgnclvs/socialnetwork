# Project Structure
```
src/
└── main/
    ├── java/
    │   └── erick/
    │       └── projects/
    │           └── socialnetwork/
    │               ├── SocialNetworkApplication.java
    │               ├── config/
    │               │   └── SecurityConfig.java
    │               ├── controller/
    │               │   ├── FollowController.java
    │               │   ├── HomeController.java
    │               │   ├── LikeController.java
    │               │   ├── PostController.java
    │               │   └── UserController.java
    │               ├── model/
    │               │   ├── Follow.java
    │               │   ├── Like.java
    │               │   ├── Post.java
    │               │   └── User.java
    │               ├── repository/
    │               │   ├── FollowRepository.java
    │               │   ├── LikeRepository.java
    │               │   ├── PostRepository.java
    │               │   └── UserRepository.java
    │               └── service/
    │                   ├── FollowService.java
    │                   ├── LikeService.java
    │                   ├── PostService.java
    │                   └── UserService.java
    └── resources/
        ├── application.properties
        └── templates/
            ├── create-post.jsp
            ├── home.jsp
            ├── login.jsp
            └── register.jsp
```

In this structure, the `SocialNetworkApplication` class is the main entry point for the app. It's located in the root package (`erick.projects.socialnetwork`).

The `config` package contains configuration classes such as `SecurityConfig`.

The `controller` package contains controller classes such as `FollowController`, `HomeController`, `LikeController`, `PostController`, and `UserController`.

The `model` package contains model classes such as `Follow`, `Like`, `Post`, and `User`.

The `repository` package contains repository interfaces such as `FollowRepository`, `LikeRepository`, `PostRepository`, and `UserRepository`.

The `service` package contains service classes such as `FollowService`, `LikeService`, `PostService`, and `UserService`.

The `resources` directory contains the `application.properties` file and the `templates` directory, which contains JSP templates such as `create-post.jsp`, `home.jsp`, `login.jsp`, and `register.jsp`.
