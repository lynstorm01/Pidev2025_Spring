package tn.esprit.blogmanagement.DTO;

import lombok.*;
import tn.esprit.blogmanagement.DTO.CommentDTO;
import tn.esprit.blogmanagement.Entity.CategoryType;
import tn.esprit.blogmanagement.Entity.ReactionType;
import tn.esprit.blogmanagement.Entity.Status;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Date lastUpdatedAt;
    private CategoryType category;
    private Long userId;
    private int nbr_like;
    private int nbr_dislike;
    private Status status = Status.PENDING;
    private String Username;
    private Map<ReactionType, Long> reactionCounts;
    private String rejectionReason;    // Added for reply IDs
    private List<Long> comments;
    private List<Long> replyIds;    // Added for reply IDs

}
