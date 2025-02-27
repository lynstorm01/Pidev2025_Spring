package tn.esprit.blogmanagement.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.blogmanagement.DTO.ReplyDTO;
import tn.esprit.blogmanagement.DTO.ReplyRequest;
import tn.esprit.blogmanagement.Entity.Comment;
import tn.esprit.blogmanagement.Entity.Reply;
import tn.esprit.blogmanagement.Entity.Post;
import tn.esprit.blogmanagement.Entity.User;
import tn.esprit.blogmanagement.Service.CommentService;
import tn.esprit.blogmanagement.Service.ReplyService;
import tn.esprit.blogmanagement.Service.PostService;
import tn.esprit.blogmanagement.Service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Reply")
public class ReplyController {

    private final ReplyService replyService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public ReplyController(ReplyService replyService, UserService userService, PostService postService,CommentService commentService) {
        this.replyService = replyService;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    // ✅ Create a new reply
    @PostMapping("/add")
    public ResponseEntity<ReplyDTO> createReply(@Valid @RequestBody ReplyRequest replyRequest) {
        try {
            // Retrieve the user by userId
            Optional<User> optionalUser = userService.getUserById(replyRequest.getUserId());
            Optional<Post> optionalPost = postService.getPostById(replyRequest.getPostId());
            Optional<Comment> optionalComment = commentService.getCommentById(replyRequest.getCommentId());

            // Check if the user exists, if not return a bad request response
            if (optionalUser.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return a bad request if user is not found
            }
            if (optionalPost.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return a bad request if Post is not found
            }
            if (optionalComment.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return a bad request if Comment is not found
            }

            // Get the User from Optional
            User user = optionalUser.get();
            // Get the Post from Optional
            Post post = optionalPost.get();
            // Get the Comment from Optional
            Comment comment = optionalComment.get();

            // Create a new Reply entity from the DTO
            Reply reply = new Reply();
            reply.setContent(replyRequest.getContent());
            reply.setCreatedAt(new Date());
            reply.setLastUpdatedAt(new Date());
            reply.setUser(user);
            reply.setPost(post);
            reply.setComment(comment);

            // Save the reply using the service
            Reply createdReply = replyService.registerReply(reply);

            // Create the ReplyDTO to return
            ReplyDTO replyDTO = new ReplyDTO(
                    createdReply.getId(),
                    createdReply.getContent(),
                    createdReply.getCreatedAt(),
                    createdReply.getLastUpdatedAt(),
                    createdReply.getIsEdited(),
                    createdReply.getUser().getId(),  // Only the userId
                    createdReply.getPost().getId() ,  // Only the postId
                    createdReply.getComment().getId()   // Only the postId
            );

            // Return the created reply as a DTO in the response body
            return ResponseEntity.status(HttpStatus.CREATED).body(replyDTO);  // Return 201 Created with the ReplyDTO
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle internal errors
        }
    }



    // ✅ Get all replys with only reply IDs
    @GetMapping("/")
    public ResponseEntity<List<ReplyDTO>> getAllReplies() {
        try {
            List<ReplyDTO> replyDTOs = replyService.getAllReplys().stream().map(reply ->
                    new ReplyDTO(
                            reply.getId(),
                            reply.getContent(),
                            reply.getCreatedAt(),
                            reply.getLastUpdatedAt(),
                            reply.getIsEdited(),
                            reply.getUser().getId(),  // Only return userId
                            reply.getComment().getPost().getId(), // Include postId
                            reply.getComment().getId()
                    )
            ).toList();

            if (replyDTOs.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 if no replys exist
            }

            return ResponseEntity.ok(replyDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // ✅ Get reply by ID (with DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ReplyDTO> getReplyById(@PathVariable Long id) {
        try {
            Optional<Reply> replyOptional = replyService.getReplyById(id);

            if (replyOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if not found
            }

            Reply reply = replyOptional.get();

            // Create a ReplyDTO with the required details (user ID, post ID, etc.)
            ReplyDTO replyDTO = new ReplyDTO(
                    reply.getId(),
                    reply.getContent(),
                    reply.getCreatedAt(),
                    reply.getLastUpdatedAt(),
                    reply.getIsEdited(),
                    reply.getUser().getId(),  // Only the user ID
                    reply.getComment().getPost().getId(),  // Only the user ID
                    reply.getComment().getId()  // Get the post ID via the comment's post
            );

            return ResponseEntity.ok(replyDTO);  // Return the reply DTO with 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return internal server error in case of an exception
        }
    }




    // ✅ Update reply
    @PutMapping("/{replyId}")
    public ResponseEntity<?> updateReply(@PathVariable Long replyId, @Valid @RequestBody Reply updatedReply) {
        try {
            Optional<Reply> existingReply = replyService.getReplyById(replyId);
            if (existingReply.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reply with ID " + replyId + " not found.");
            }

            // Set the updatedAt field to current time
            updatedReply.setLastUpdatedAt(new Date());  // Ensure the updatedAt is set before the update
            updatedReply.setIsEdited(true);  // Ensure the updatedAt is set before the update

            Reply reply = replyService.updateReply(replyId, updatedReply);
            return ResponseEntity.ok(reply);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the reply.");
        }
    }

    // ✅ Delete reply
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReply(@PathVariable Long id) {
        try {
            if (replyService.getReplyById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reply with ID " + id + " not found.");
            }
            replyService.deleteReply(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Reply deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the reply.");
        }
    }
}
