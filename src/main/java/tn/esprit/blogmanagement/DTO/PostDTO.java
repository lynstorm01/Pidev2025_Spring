package tn.esprit.blogmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.blogmanagement.DTO.CommentDTO;
import tn.esprit.blogmanagement.Entity.CategoryType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Date lastUpdatedAt;
    private CategoryType category;
    private Long userId;
    private List<Long> comments;
    private List<Long> replyIds;    // Added for reply IDs



}
