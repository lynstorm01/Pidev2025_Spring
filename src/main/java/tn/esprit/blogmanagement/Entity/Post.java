package tn.esprit.blogmanagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "Creation date cannot be in the future")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();  // Set default to current date

    // Add UpdatedAt field to store last updated time
    @Temporal(TemporalType.TIMESTAMP)
    private Date LastUpdatedAt = new Date();  // Set default to current date

    // Number of likes (default 0)
    @Column(nullable = false)
    private int numberOfLikes = 0;

    // Number of dislikes (default 0)
    @Column(nullable = false)
    private int numberOfDislikes = 0;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is required")
    @Column(nullable = false)
    private CategoryType category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    @NotNull(message = "User is required")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

}
