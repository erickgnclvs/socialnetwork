package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Image;
import erick.projects.socialnetwork.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {
    private final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        // Find the image in the database
        Image img = imageRepository.findById(imageId).orElseThrow();
        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(img.getImageType()));
        headers.setContentLength(img.getImage().length);

        // Return the image as a byte array
        return new ResponseEntity<>(img.getImage(), headers, HttpStatus.OK);
    }

}
