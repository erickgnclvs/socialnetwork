package erick.projects.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * This Class represents the Users and builds the table in the database for them
 */

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 20)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;
    private String biography;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    /**
     * The following fields are yet to be implemented
     */
    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes;
    @OneToMany(mappedBy = "follower")
    private List<Follow> following;
    @OneToMany(mappedBy = "followee")
    private List<Follow> followers;
}
