package erick.projects.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A JPA entity representing a post.
 */
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 280)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    public Post(Long id, String content, LocalDateTime createdAt, User user) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Post() {
    }

    public boolean isLikedBy(User user) {
        if (likes == null) {
            return false;
        }
        return likes.stream().anyMatch(like -> like.getUser().equals(user));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
