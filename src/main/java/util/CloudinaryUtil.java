package util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryUtil {
    private static Cloudinary cloudinary;

    public static void main(String[] args) throws Exception {
        // тестирование загрузки файла
        byte[] fileBytes;
        Path filePath = Paths.get("image.jpeg");

        try {
            fileBytes = Files.readAllBytes(filePath);
            String url = uploadImage(fileBytes);
            if (url != null) {
                System.out.println(url);
            } else {
                System.err.println("uploadImage returned null");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            Dotenv dotenv = Dotenv.load();
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", dotenv.get("CLOUD_NAME"));
            config.put("api_key", dotenv.get("API_KEY"));
            config.put("api_secret", dotenv.get("API_SECRET"));
            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }
    public static String uploadImage(byte[] imageBytes) throws Exception {
        Cloudinary cloudinary = getInstance();
        Map params = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );
        return (String) cloudinary.uploader().upload(imageBytes, params).get("url");
    }
}
