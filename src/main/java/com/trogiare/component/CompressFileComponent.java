package com.trogiare.component;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class CompressFileComponent {
    public byte[] compressImage(MultipartFile input,Float uitDivide) throws IOException {
        BufferedImage image = ImageIO.read(input.getInputStream());

        // Set the compression quality to 0.5 (50%).
        float compressionQuality = 0.5f;
        // Scale the image to half its size using imgscalr library.
        BufferedImage scaledImage = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH,Math.round(image.getWidth() / uitDivide),Math.round(image.getHeight() / uitDivide)  , Scalr.OP_ANTIALIAS);

        // Write the scaled image to a ByteArrayOutputStream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(scaledImage, "jpeg", outputStream);

        // Get the compressed image as a byte array.
        byte[] compressedImage = outputStream.toByteArray();

        // Close the ByteArrayOutputStream and return the compressed image.
        outputStream.close();
        return compressedImage;
    }
}
