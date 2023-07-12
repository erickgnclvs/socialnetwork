package erick.projects.socialnetwork.model;

import jakarta.persistence.*;

/**
 * A JPA entity representing an image.
 */
@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long id;
    private String imageName;
    private String imageType;
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    @OneToOne(mappedBy = "profileImage")
    private User user;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
