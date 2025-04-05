package tn.esprit.blogmanagement.Service;

import tn.esprit.blogmanagement.DTO.ReactionCountDTO;
import tn.esprit.blogmanagement.DTO.ReactionDTO;
import tn.esprit.blogmanagement.Entity.*;
import tn.esprit.blogmanagement.Repository.ReactionRepository;
import tn.esprit.blogmanagement.Repository.PostRepository;
import tn.esprit.blogmanagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReactionService implements IReactionService {
    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReactionDTO addOrUpdateReaction(Long postId, ReactionType type, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if a reaction already exists
        Optional<Reaction> existingReaction = reactionRepository.findByUserAndPost(user, post);

        if (existingReaction.isPresent()) {
            // Update the existing reaction
            existingReaction.get().setType(type);
            Reaction saved = reactionRepository.save(existingReaction.get());
            return mapToDTO(saved);
        }

        // Create new reaction if none exists
        Reaction reaction = Reaction.builder()
                .type(type)
                .user(user)
                .post(post)
                .createdAt(new Date())
                .build();

        Reaction saved = reactionRepository.save(reaction);
        return mapToDTO(saved);
    }


    @Override
    @Transactional
    public void removeReaction(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        reactionRepository.findByUserAndPost(user, post)
                .ifPresent(reactionRepository::delete);
    }

    @Override
    public Map<ReactionType, Long> getReactionCounts(Long postId) {
        List<ReactionCountDTO> counts = reactionRepository.countReactionsByType(postId);

        // Convert to map with all possible reaction types initialized to 0
        Map<ReactionType, Long> result = Arrays.stream(ReactionType.values())
                .collect(Collectors.toMap(
                        type -> type,
                        type -> 0L
                ));

        // Update with actual counts
        counts.forEach(dto -> result.put(dto.getType(), dto.getCount()));

        return result;
    }

    @Override
    public ReactionType getUserReaction(Long postId, Long userId) {
        return reactionRepository.findTypeByUserIdAndPostId(userId, postId)
                .orElse(null);
    }

    // Change from private to public
    public ReactionDTO mapToDTO(Reaction reaction) {
        return ReactionDTO.builder()
                .id(reaction.getId())
                .type(reaction.getType())
                .userId(reaction.getUser().getId())
                .postId(reaction.getPost().getId())
                .createdAt(reaction.getCreatedAt().toString())
                .build();
    }
}
