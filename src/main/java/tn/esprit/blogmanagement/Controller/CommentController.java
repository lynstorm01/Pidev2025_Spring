package tn.esprit.blogmanagement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.blogmanagement.DTO.CommentDTO;
import tn.esprit.blogmanagement.DTO.CommentRequest;
import tn.esprit.blogmanagement.Entity.Comment;
import jakarta.validation.Valid;
import tn.esprit.blogmanagement.Entity.Post;
import tn.esprit.blogmanagement.Entity.User;
import tn.esprit.blogmanagement.Service.CommentService;
import tn.esprit.blogmanagement.Service.PostService;
import tn.esprit.blogmanagement.Service.UserService;
import tn.esprit.blogmanagement.Service.mailService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;
    private final tn.esprit.blogmanagement.Service.mailService mailService;


    public CommentController(CommentService commentService, UserService userService, PostService postService, mailService mailService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
        this.mailService = mailService;
    }

    // ✅ Create a new comment
    @PostMapping("/add")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentRequest commentRequest) {
        try {
            // Retrieve the user and post by IDs
            Optional<User> optionalUser = userService.getUserById(commentRequest.getUserId());
            Optional<Post> optionalPost = postService.getPostById(commentRequest.getPostId());

            if (optionalUser.isEmpty() || optionalPost.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return bad request if user or post not found
            }

            User user = optionalUser.get();
            Post post = optionalPost.get();

            // Create a new comment entity
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            comment.setCreatedAt(new Date());
            comment.setUser(user);
            comment.setPost(post);
            comment.setReplies(List.of());
            comment.setGifUrl(commentRequest.getGifUrl());  // Store GIF URL


            // Save the comment
            Comment createdComment = commentService.registerComment(comment);

            // ✅ Handle Mentions (NEW)
            List<String> mentionedUsernames = commentService.extractMentions(commentRequest.getContent());
            System.out.println(mentionedUsernames);
            if (!mentionedUsernames.isEmpty()) {
                List<User> mentionedUsers = userService.findByUsernameIn(mentionedUsernames);
                System.out.println(mentionedUsers);
                mentionedUsers.forEach(mentionedUser ->
                        mailService.sendMentionNotification(mentionedUser.getEmail(), comment.getContent(),mentionedUser.getUsername())
                );
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // ✅ Get all comments with only reply IDs
    @GetMapping("/")
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        try {
            List<CommentDTO> commentDTOs = commentService.getAllComments().stream().map(comment ->
                    new CommentDTO(
                            comment.getId(),
                            comment.getContent(),
                            comment.getCreatedAt(),
                            comment.getLastUpdatedAt(),
                            comment.getIsEdited(),
                            comment.getUser().getId(),  // Only return userId
                            comment.getPost().getId(),  // Include postId
                            comment.getUser().getUsername(),
                            comment.getPost().getTitle(),
                            comment.getGifUrl(),
                            comment.getReplies().stream().map(reply ->
                                    reply.getId()  // Only return the reply ID
                            ).toList()
                    )
            ).toList();

            if (commentDTOs.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 if no comments exist
            }

            return ResponseEntity.ok(commentDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // ✅ Get comment by ID (with DTO)
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        try {
            Optional<Comment> commentOptional = commentService.getCommentById(id);

            if (commentOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if not found
            }

            Comment comment = commentOptional.get();

            // Create a CommentDTO with the required details (user ID, post ID, etc.)
            CommentDTO commentDTO = new CommentDTO(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getLastUpdatedAt(),
                    comment.getIsEdited(),
                    comment.getUser().getId(),  // Only the user ID
                    comment.getPost().getId(),  // Post ID
                    comment.getUser().getUsername(),
                    comment.getPost().getTitle(),
                    comment.getGifUrl(),
                    comment.getReplies().stream().map(reply -> reply.getId()).collect(Collectors.toList()) // List of reply IDs
            );

            return ResponseEntity.ok(commentDTO);  // Return the comment DTO with 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return internal server error in case of an exception
        }
    }



    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequest commentRequest) {
        try {
            Optional<Comment> existingComment = commentService.getCommentById(commentId);
            if (existingComment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Comment with ID " + commentId + " not found.");
            }

            // Fetch the User by userId from the PostDTO
            Optional<User> user = userService.getUserById(commentRequest.getUserId());  // Assuming userService.getUserById is available
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User with ID " + commentRequest.getUserId() + " not found.");
            }

            Optional<Post> post = postService.getPostById(commentRequest.getPostId());
            if (post.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Post with ID " + commentRequest.getPostId() + " not found.");
            }

            Comment commentToUpdate = existingComment.get();

            // Update fields using the request DTO
            commentToUpdate.setContent(commentRequest.getContent());
            commentToUpdate.setPost(post.get());
            commentToUpdate.setUser(user.get());
            commentToUpdate.setLastUpdatedAt(new Date());
            commentToUpdate.setIsEdited(true);

            // Save updated comment
            Comment updatedComment = commentService.updateComment(commentId, commentToUpdate);
            return ResponseEntity.ok(updatedComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the comment.");
        }
    }


    // ✅ Delete comment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            if (commentService.getCommentById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Comment with ID " + id + " not found.");
            }
            commentService.deleteComment(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Comment deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the comment.");
        }
    }
}
