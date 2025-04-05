package tn.esprit.blogmanagement.DTO;

import lombok.*;
import tn.esprit.blogmanagement.Entity.ReactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionDTO {
    private Long id;
    private ReactionType type;
    private Long userId;
    private Long postId;
    private String createdAt;
}
