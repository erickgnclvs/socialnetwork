# Sprint 1: Project setup and user registration

**Step 1: Set up your development environment**
- Install IntelliJ Ultimate and MySQL on your computer

**Step 2: Create a new Spring Boot project**
- Open IntelliJ and create a new project
- Select Spring Initializr as the project type
- Choose your project options (e.g., project SDK, language, packaging)
- Add the following dependencies: Spring Web, Spring Data JPA, MySQL Driver, Thymeleaf

**Step 3: Set up the database connection**
- Open the `application.properties` file in your project
- Add the following properties to configure the database connection:
```
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
```

**Step 4: Implement user registration**
- Create a `User` model class to represent a user in your app. This class should have fields for the user's information (e.g., username, email, password) and should be annotated with `@Entity` to indicate that it's a JPA entity.
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    // getters and setters
}
```
- Create a `UserRepository` interface that extends `JpaRepository`. This interface provides methods for performing CRUD (Create, Read, Update, Delete) operations on `User` objects.
```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```
- Create a `UserService` class that provides methods for interacting with `User` objects. This class should use the `UserRepository` to perform database operations.
```java
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
```
- Create a `UserController` class that handles HTTP requests related to users. This class should use the `UserService` to perform business logic.
```java
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
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
        userService.saveUser(user);
        return "redirect:/login";
    }
}
```
- Create a registration form in JSP. This form should allow users to enter their information (e.g., username, email, password) and submit it to the server.
```jsp
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Registration</h1>
<form:form method="POST" action="/register" modelAttribute="user">
    <table>
        <tr>
            <td>Username:</td>
            <td><form:input path="username"/></td>
        </tr>
        <tr>
            <td>Email:</td>
            <td><form:input path="email"/></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><form:password path="password"/></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="Register"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>
```
- In the `UserController`, validate the user input (e.g., check if the username is already taken) and use the `UserService` to save the user to the database.