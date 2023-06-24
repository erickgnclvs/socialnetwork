# Social Network App

This is a simple social network app built with Java, Spring, MySQL, JSP, and Bootstrap. It allows users to register, login, post text-based content, view a feed of posts from followed users, follow/unfollow other users, and like posts.

## Organization files

All the files related to organization are in the directory ``organization``


## Getting Started

To get started with this project, follow these steps:

1. Install IntelliJ Ultimate and MySQL on your computer.
2. Clone this repository and open it in IntelliJ.
3. Create a new MySQL database for the app.
4. Update the `application.properties` file with your database connection information.
5. Run the app using the `./mvnw spring-boot:run` command.

## Sprints

The development of this app was divided into several sprints. Each sprint focused on implementing a specific set of features.

### Sprint 1: Project setup and user registration

In this sprint, we set up the development environment and implemented user registration.

- Set up IntelliJ Ultimate and MySQL
- Created a new Spring Boot project with the following dependencies: Spring Web, Spring Data JPA, MySQL Driver, Thymeleaf
- Configured the database connection in the `application.properties` file
- Implemented user registration using a `User` model class, a `UserRepository` interface, a `UserService` class, and a `UserController` class
- Created a registration form in JSP and handled form submission in the `UserController`

### Sprint 2: User login and security

In this sprint, we implemented user login and added security to the app.

- Created a login form in JSP and handled form submission in the `UserController`
- Authenticated users using Spring Security's `AuthenticationManager`
- Added the Spring Security dependency to the project
- Configured Spring Security in a `SecurityConfig` class to restrict access to certain pages or APIs
- Implemented the `UserDetailsService` interface in the `UserService` class to load users by their username

### Sprint 3: Posting and viewing content

In this sprint, we implemented posting text-based content and viewing a feed of posts from followed users.

- Implemented posting text-based content using a `Post` model class, a `PostRepository` interface, a `PostService` class, and a `PostController` class
- Created a form for creating posts in JSP and handled form submission in the `PostController`
- Implemented viewing a feed of posts from followed users by retrieving posts from the database using the `PostService` and displaying them in the frontend using JSP and Bootstrap components

### Sprint 4: Following and unfollowing users

In this sprint, we implemented following/unfollowing other users.

- Implemented following/unfollowing other users using a `Follow` model class, a `FollowRepository` interface, a `FollowService` class, and a `FollowController` class
- Added buttons for following/unfollowing users in JSP pages and handled button clicks in the `FollowController`
- Used the `FollowService` to create or delete `Follow` objects when a user follows or unfollows another user

### Sprint 5: Implementing likes

In this sprint, we implemented liking posts.

- Implemented liking posts using a `Like` model class, a `LikeRepository` interface, a `LikeService` class, and a `LikeController` class
- Added buttons for liking/unliking posts in JSP pages and handled button clicks in the `LikeController`
- Used the `LikeService` to create or delete `Like` objects when a user likes or unlikes a post

## Next Steps

There are many additional features and improvements that could be made to this app. Some ideas for next steps include:

- Improving the user interface by customizing Bootstrap components and adding CSS styles
- Adding more functionality such as commenting on posts or sending private messages between users
- Improving security by adding features such as password hashing or CSRF protection
- Testing and debugging the app to ensure it's working correctly
- Deploying the app to a server so that other people can use it
