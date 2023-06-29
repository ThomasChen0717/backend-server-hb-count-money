package admin.server.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Base64Utils {
    public static String convertToBase64(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        byte[] base64Bytes = Base64.encodeBase64(fileBytes);
        return new String(base64Bytes, StandardCharsets.UTF_8);
    }

    public static String convertToBase64(String Url) throws IOException {
        URL url = new URL(Url);
        try (InputStream inputStream = url.openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[65535];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeBase64String(imageBytes);
        }
    }

}