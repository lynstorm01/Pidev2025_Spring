package tn.esprit.blogmanagement.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GifService {

    private final ResourceLoader resourceLoader;

    @Value("${gif.upload.directory}")
    private String gifUploadDirectory;

    public GifService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public List<String> getAllGifs() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(gifUploadDirectory))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".gif"))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    public Resource getGif(String gifName) throws IOException {
        // First try to load from file system (works in development)
        try {
            Path filePath = Paths.get("src/main/resources/static/assets/gifs/" + gifName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            // Fall through to classpath loading
        }

        // Fall back to classpath loading (works in production)
        return resourceLoader.getResource("classpath:/static/assets/gifs/" + gifName);
    }

    public String uploadGif(MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getContentType().equals("image/gif")) {
            throw new IllegalArgumentException("Only GIF files are allowed");
        }

        Path uploadPath = Paths.get(gifUploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }
}