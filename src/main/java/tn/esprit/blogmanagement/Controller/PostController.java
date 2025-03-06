package tn.esprit.blogmanagement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.blogmanagement.DTO.PostDTO;
import tn.esprit.blogmanagement.DTO.PostRequest;
import tn.esprit.blogmanagement.Entity.Comment;
import tn.esprit.blogmanagement.Entity.Post;
import jakarta.validation.Valid;
import tn.esprit.blogmanagement.Entity.Status;
import tn.esprit.blogmanagement.Entity.User;
import tn.esprit.blogmanagement.Repository.PostRepository;
import tn.esprit.blogmanagement.Service.CommentService;
import tn.esprit.blogmanagement.Service.PostService;
import tn.esprit.blogmanagement.Service.UserService;
import tn.esprit.blogmanagement.Service.mailService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/Blog")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;
    private final tn.esprit.blogmanagement.Service.mailService mailService;
    private final CommentService commentService;

    public PostController(PostService postService, UserService userService, PostRepository postRepository, mailService mailService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.postRepository = postRepository;
        this.mailService = mailService;
        this.commentService = commentService;
    }

    // ✅ Create a new post
    @PostMapping("/add")
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostRequest postRequest) {
        try {
            // Retrieve the user by userId
            Optional<User> optionalUser = userService.getUserById(postRequest.getUserId());

            // Check if the user exists, if not return a bad request response
            if (optionalUser.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return a bad request if user is not found
            }

            // Get the User from Optional
            User user = optionalUser.get();

            // Create a new Post entity from the DTO
            Post post = new Post();
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            post.setCategory(postRequest.getCategory());
            post.setUser(user); // Set the user by userId
            post.setComments(List.of()); // Ensure comments are set as an empty list if none provided
            post.setStatus(Status.PENDING);

            // Save the post using the service
            Post createdPost = postService.registerPost(post);

            mailService.sendPostPendingEmail(post.getUser().getEmail(), postRequest.getTitle(),post.getUser().getUsername());
                System.out.println(" Post submitted and email sent to "+ post.getUser().getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost); // Return the created post
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle internal errors
        }
    }


    // ✅ Get all posts with only userId, commentIds, and replyIds
    @GetMapping("/")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        try {
            List<PostDTO> postDTOs = postService.getAllPosts().stream().map(post ->
                    new PostDTO(
                            post.getId(),
                            post.getTitle(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getLastUpdatedAt(),
                            post.getCategory(),
                            post.getUser().getId(),  // Only return userId
                            post.getNumberOfLikes(),
                            post.getNumberOfDislikes(),
                            post.getStatus(),
                            post.getUser().getUsername(),
                            post.getComments().stream().map(comment ->
                                    comment.getId()  // Only return comment ID
                            ).toList(),
                            post.getComments().stream().flatMap(comment ->
                                    comment.getReplies().stream().map(reply ->
                                            reply.getId()  // Only return reply ID
                                    )
                            ).toList()
                    )
            ).toList();

            if (postDTOs.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 if no posts exist
            }

            return ResponseEntity.ok(postDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // ✅ Get post by ID with only userId, commentIds, and replyIds
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        try {
            Optional<Post> postOptional = postService.getPostById(id);

            if (postOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Returns 404 if post not found
            }

            Post post = postOptional.get();

            // Map the found post to PostDTO
            PostDTO postDTO = new PostDTO(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt(),
                    post.getLastUpdatedAt(),
                    post.getCategory(),
                    post.getUser().getId(),  // Only return userId
                    post.getNumberOfLikes(),
                    post.getNumberOfDislikes(),
                    post.getStatus(),
                    post.getUser().getUsername(),
                    post.getComments().stream().map(comment ->
                            comment.getId()  // Only return comment ID
                    ).toList(),
                    post.getComments().stream().flatMap(comment ->
                            comment.getReplies().stream().map(reply ->
                                    reply.getId()  // Only return reply ID
                            )
                    ).toList()  // Collect reply IDs in a separate list
            );

            return ResponseEntity.ok(postDTO);  // Return the PostDTO in response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Return 500 in case of errors
        }
    }




    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @Valid @RequestBody PostRequest updatedPostDTO) {
        try {
            // Fetch the existing post
            Optional<Post> existingPost = postService.getPostById(postId);
            if (existingPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Post with ID " + postId + " not found.");
            }

            // Fetch the User by userId from the PostDTO
            Optional<User> user = userService.getUserById(updatedPostDTO.getUserId());  // Assuming userService.getUserById is available
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User with ID " + updatedPostDTO.getUserId() + " not found.");
            }

            // Map the PostDTO to Post entity
            Post updatedPost = new Post();
            updatedPost.setId(postId); // Retain the existing post ID
            updatedPost.setUser(user.get()); // Set the user ID from DTO
            updatedPost.setTitle(updatedPostDTO.getTitle()); // Set title from DTO
            updatedPost.setContent(updatedPostDTO.getContent()); // Set content from DTO
            updatedPost.setCategory(updatedPostDTO.getCategory()); // Set category from DTO
            updatedPost.setComments(updatedPostDTO.getComments()); // Set comments from DTO if any
            updatedPost.setLastUpdatedAt(new Date()); // Set the updated timestamp

            // Call the service to update the post
            Post post = postService.updatePost(postId, updatedPost);

            // Return the updated post
            return ResponseEntity.ok(post);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the post.");
        }
    }


    // ✅ Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            if (postService.getPostById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Post with ID " + id + " not found.");
            }
            postService.deletePost(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Post deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the post.");
        }
    }

    @PutMapping("/update-status/{postId}")
    public ResponseEntity<?> updatePostStatus(@PathVariable Long postId, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        Optional<Post> optionalPost = postService.getPostById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setStatus(Status.valueOf(newStatus)); // Convert String to Enum
            postService.registerPost(post);
            String PostLink = "http://localhost:4200/blogs";
            if(newStatus.equals("APPROVED")) {
                mailService.sendPostApprovedEmail(post.getUser().getEmail(), post.getUser().getUsername(),post.getTitle(), PostLink);
                System.out.println(" Post Approvel and email sent to "+ post.getUser().getUsername());
            }
            if(newStatus.equals("REJECTED")) {
                mailService.sendPostRejectedEmail(post.getUser().getEmail(), post.getUser().getUsername(),post.getTitle());
                System.out.println(" Post Rejection and email sent to "+ post.getUser().getUsername());
            }
            return ResponseEntity.ok("Post status updated successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
    }

    // Endpoint to get comments for a specific post
    @GetMapping("/{postId}/comments")
    public List<Comment> getCommentsForPost(@PathVariable Long postId) {
        return commentService.getCommentsForPost(postId);
    }
}
