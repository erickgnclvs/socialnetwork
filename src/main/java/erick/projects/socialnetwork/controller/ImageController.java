package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Image;
import erick.projects.socialnetwork.repository.ImageRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Controller for handling HTTP requests related to images.
 */
@Controller
public class ImageController {
    private final ImageRepository imageRepository;

    /**
     * Constructor for ImageController.
     *
     * @param imageRepository the repository for accessing images in the database
     */
    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Retrieves an image from the database, crops it to a square, and returns it as a byte array in the response body.
     *
     * @param imageId the ID of the image to retrieve
     * @return the cropped image as a byte array
     */
    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        // Find the image in the database
        Image img = imageRepository.findById(imageId).orElseThrow();

        try {
            // Convert the image byte array to a BufferedImage
            ByteArrayInputStream bais = new ByteArrayInputStream(img.getImage());
            BufferedImage originalImage = ImageIO.read(bais);

            // Calculate the size of the square
            int squareSize = Math.min(originalImage.getWidth(), originalImage.getHeight());

            // Calculate the coordinates of the center of the image
            int xCenter = originalImage.getWidth() / 2;
            int yCenter = originalImage.getHeight() / 2;

            // Calculate the top left coordinates of the square
            int x = xCenter - (squareSize / 2);
            int y = yCenter - (squareSize / 2);

            // Crop the image to a square
            BufferedImage croppedImage = originalImage.getSubimage(x, y, squareSize, squareSize);

            // Convert the cropped image to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(croppedImage, img.getImageType().split("/")[1], baos);
            byte[] imageBytes = baos.toByteArray();

            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(img.getImageType()));
            headers.setContentLength(imageBytes.length);

            // Return the cropped image as a byte array
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle any errors
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns a default profile picture as a byte array in the response body.
     *
     * @return the default profile picture as a byte array
     */
    @GetMapping("/images/default")
    public ResponseEntity<byte[]> getDefaultImage() {
        // Load the default image from the classpath
        Resource resource = new ClassPathResource("/static/images/default-profile-picture.png");
        try {
            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(resource.contentLength());

            // Return the image as a byte array
            return new ResponseEntity<>(IOUtils.toByteArray(resource.getInputStream()), headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle any errors
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/images/error")
    public ResponseEntity<byte[]> getErrorImage() {
        // Load the default image from the classpath
        Resource resource = new ClassPathResource("/static/images/error.png");
        try {
            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(resource.contentLength());

            // Return the image as a byte array
            return new ResponseEntity<>(IOUtils.toByteArray(resource.getInputStream()), headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle any errors
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
