package tn.esprit.blogmanagement.DTO;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {
    private Long id;
    private String content;
    private Date createdAt;
    private Date lastUpdatedAt;
    private Boolean isEdited;
    private Long userId;   // Only return userId, not full User details
    private String username;
    private Long postId;   // Only return userId, not full User details
    private String postTitle;
    private Long CommentId;   // Only return userId, not full User details
    private String commentContent;

}
