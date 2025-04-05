// src/main/java/tn/esprit/blogmanagement/Controller/ReactionController.java
package tn.esprit.blogmanagement.Controller;

import org.springframework.http.HttpStatus;
import tn.esprit.blogmanagement.DTO.ReactionDTO;
import tn.esprit.blogmanagement.Entity.ReactionType;
import tn.esprit.blogmanagement.Service.IReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final IReactionService reactionService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> reactToPost(
            @PathVariable Long postId,
            @RequestParam ReactionType type,
            @RequestHeader("X-User-ID") Long userId) {
        ReactionDTO reaction = reactionService.addOrUpdateReaction(postId, type, userId);
        if (reaction == null) {
            return ResponseEntity.ok().body("Reaction removed");
        }
        return ResponseEntity.ok(reaction);
    }

    @GetMapping("/{postId}/counts")
    public ResponseEntity<Map<ReactionType, Long>> getReactionCounts(@PathVariable Long postId) {
        return ResponseEntity.ok(reactionService.getReactionCounts(postId));
    }

    @GetMapping("/{postId}/user")
    public ResponseEntity<ReactionType> getUserReaction(
            @PathVariable Long postId,
            @RequestHeader("X-User-ID") Long userId) {
        return ResponseEntity.ok(reactionService.getUserReaction(postId, userId));
    }

    @PostMapping("/{postId}/remove-reaction")
    public ResponseEntity<?> removeReaction(
            @PathVariable Long postId,
            @RequestHeader("X-User-ID") Long userId) {

        try {
            // Remove the reaction via the service
            reactionService.removeReaction(postId, userId);

            // Return a JSON response with a message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Reaction removed successfully");

            return ResponseEntity.ok().body(response); // Return JSON response
        } catch (RuntimeException e) {
            // Handle any errors that occur during the process
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error removing reaction: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse); // Return error as JSON
        }
    }


}
