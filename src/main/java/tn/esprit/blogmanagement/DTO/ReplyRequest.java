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
public class ReplyRequest {

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Comment ID is required")
    private Long CommentId;

    @NotBlank(message = "Comment content cannot be empty")
    @Size(min = 5, max = 500, message = "Comment must be between 5 and 500 characters")
    private String content;

}
