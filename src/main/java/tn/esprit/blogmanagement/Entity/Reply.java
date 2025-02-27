package tn.esprit.blogmanagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Reply content cannot be empty")
    @Size(min = 3, max = 500, message = "Reply must be between 3 and 500 characters")
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "Creation date cannot be in the future")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date(); // Default to current date

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt = new Date(); // Default to current date

    private Boolean isEdited = false; // Default to false

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    @JsonBackReference
    @NotNull(message = "Comment is required")
    private Comment comment; // Each reply is linked to a comment

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    // Add the postId attribute
    @Transient // The @Transient annotation tells JPA not to persist this field in the database.
    private Post post;

}
