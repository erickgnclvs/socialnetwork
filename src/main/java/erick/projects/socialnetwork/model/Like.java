package erick.projects.socialnetwork.model;

import jakarta.persistence.*;

/**
 * A JPA entity representing like relationships between users and posts.
 */
@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
public class Like {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Like() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
