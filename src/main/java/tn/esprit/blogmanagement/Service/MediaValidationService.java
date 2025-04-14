package tn.esprit.blogmanagement.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

@Service
public class MediaValidationService {

    // 2MB max size
    private static final long MAX_FILE_SIZE = 20 * 1024;

    // Allowed image types
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif"
    );

    // Allowed video types (optional)
    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4",
            "video/quicktime"
    );

    public void validateMediaFile(MultipartFile file) {
        // First check if file is null or empty
        if (file == null || file.isEmpty() || file.getSize() == 0) {
            return; // Exit early if no file is provided
        }

        // Check size only if there's an actual file
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds 20KB limit");
        }

        // Check type only if there's an actual file
        String contentType = file.getContentType();

        // Add null check for contentType
        if (contentType == null) {
            throw new IllegalArgumentException("File type could not be determined");
        }

        boolean isImage = ALLOWED_IMAGE_TYPES.contains(contentType);
        boolean isVideo = ALLOWED_VIDEO_TYPES.contains(contentType);

        if (!isImage && !isVideo) {
            throw new IllegalArgumentException(
                    "Unsupported file type. Allowed: " +
                            String.join(", ", ALLOWED_IMAGE_TYPES) +
                            (ALLOWED_VIDEO_TYPES.isEmpty() ? "" : " and videos: " +
                                    String.join(", ", ALLOWED_VIDEO_TYPES))
            );
        }
    }
}