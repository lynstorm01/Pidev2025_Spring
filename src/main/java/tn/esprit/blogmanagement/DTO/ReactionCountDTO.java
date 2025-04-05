package tn.esprit.blogmanagement.DTO;

import tn.esprit.blogmanagement.Entity.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReactionCountDTO {
    private ReactionType type;
    private Long count;
}
