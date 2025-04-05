package tn.esprit.blogmanagement.Service;

import tn.esprit.blogmanagement.DTO.ReactionDTO;
import tn.esprit.blogmanagement.Entity.ReactionType;

import java.util.Map;

public interface IReactionService {
    ReactionDTO addOrUpdateReaction(Long postId, ReactionType type, Long userId);
    void removeReaction(Long postId, Long userId);
    Map<ReactionType, Long> getReactionCounts(Long postId);
    ReactionType getUserReaction(Long postId, Long userId);
}
