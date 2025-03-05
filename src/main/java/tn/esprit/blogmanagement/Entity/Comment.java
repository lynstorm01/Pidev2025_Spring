package tn.esprit.blogmanagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Comment content cannot be empty")
    @Size(min = 5, max = 500, message = "Comment must be between 5 and 500 characters")
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "Creation date cannot be in the future")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date(); // Default to current date

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt ; // Default to current date

    private Boolean isEdited = false; // Default to false

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference
    @NotNull(message = "Post is required")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Reply> replies; // Each comment can have multiple replies
}
