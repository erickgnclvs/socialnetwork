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
    @Column(nullable = false)
    private boolean isActive;

    /**
     *
     * fields not implemented yet
     *
    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes;
    @OneToMany(mappedBy = "follower")
    private List<Follow> following;
    @OneToMany(mappedBy = "followee")
    private List<Follow> followers;
    **/

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
