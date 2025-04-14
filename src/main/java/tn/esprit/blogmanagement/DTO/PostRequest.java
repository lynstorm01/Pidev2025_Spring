package tn.esprit.blogmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.blogmanagement.Entity.CategoryType;
import tn.esprit.blogmanagement.Entity.Comment;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;

    @NotNull(message = "Category is required")
    private CategoryType category;

    private List<Comment> comments = List.of(); // Default to empty list if no comments are provided

    // Add filter result fields
    private Boolean containsBadWords;
    private Set<String> badWords;
    private Boolean insuranceRelated;
    private Boolean duplicateContent;
    private Integer duplicateCount;
    private List<PostDTO> similarPosts;


}
