package tn.esprit.blogmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.blogmanagement.Entity.Reply;

import java.util.List;

@Getter
@Setter
public class CommentRequest {

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Comment content cannot be empty")
    @Size(min = 5, max = 500, message = "Comment must be between 5 and 500 characters")
    private String content;

    private List<Reply> replies; // List of replies associated with this comment

    private List<String> mentions; // List of mentioned usernames


}
