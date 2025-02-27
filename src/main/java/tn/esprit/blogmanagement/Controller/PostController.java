package tn.esprit.blogmanagement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.blogmanagement.DTO.PostDTO;
import tn.esprit.blogmanagement.DTO.PostRequest;
import tn.esprit.blogmanagement.Entity.Post;
import jakarta.validation.Valid;
import tn.esprit.blogmanagement.Entity.User;
import tn.esprit.blogmanagement.Service.PostService;
import tn.esprit.blogmanagement.Service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Blog")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
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

            // Save the post using the service
            Post createdPost = postService.registerPost(post);

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




    // ✅ Update post
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @Valid @RequestBody Post updatedPost) {
        try {
            Optional<Post> existingPost = postService.getPostById(postId);
            if (existingPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Post with ID " + postId + " not found.");
            }

            // Set the updatedAt field to current time
            updatedPost.setLastUpdatedAt(new Date());  // Ensure the updatedAt is set before the update

            Post post = postService.updatePost(postId, updatedPost);
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
}
