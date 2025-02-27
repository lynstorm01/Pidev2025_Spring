package tn.esprit.blogmanagement.DTO;

import lombok.*;
import tn.esprit.blogmanagement.Entity.Reply;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Date createdAt;
    private Date lastUpdatedAt;
    private Boolean isEdited;
    private Long userId;   // Only return userId, not full User details
    private Long postId;   // Include the post ID
    private List<Long> repliesId;  // Include replies if needed
}
