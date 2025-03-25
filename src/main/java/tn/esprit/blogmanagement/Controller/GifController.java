package tn.esprit.blogmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.blogmanagement.Service.GifService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/gifs")
public class GifController {

    @Autowired
    private GifService gifService;

    @GetMapping
    public List<String> getAllGifs() throws IOException {
        return gifService.getAllGifs();
    }

    @GetMapping("/{gifName:.+}") // The ".+" ensures file extensions work
    public ResponseEntity<Resource> getGif(@PathVariable String gifName) throws IOException {
        Resource resource = gifService.getGif(gifName);

        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GIF not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + gifName + "\"")
                .body(resource);
    }

    @PostMapping("/upload")
    public String uploadGif(@RequestParam("file") MultipartFile file) throws IOException {
        return gifService.uploadGif(file);
    }
}