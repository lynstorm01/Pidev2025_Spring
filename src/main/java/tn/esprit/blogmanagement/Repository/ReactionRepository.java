package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.blogmanagement.DTO.ReactionCountDTO;
import tn.esprit.blogmanagement.Entity.Post;
import tn.esprit.blogmanagement.Entity.Reaction;
import tn.esprit.blogmanagement.Entity.ReactionType;
import tn.esprit.blogmanagement.Entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndPost(User user, Post post);

    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.post.id = :postId AND r.type = :type")
    long countByPostAndType(@Param("postId") Long postId, @Param("type") ReactionType type);

    boolean existsByUserAndPostAndType(User user, Post post, ReactionType type);

    @Query("SELECT r.type FROM Reaction r WHERE r.user.id = :userId AND r.post.id = :postId")
    Optional<ReactionType> findTypeByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    @Query("SELECT NEW tn.esprit.blogmanagement.DTO.ReactionCountDTO(r.type, COUNT(r)) " +
            "FROM Reaction r WHERE r.post.id = :postId " +
            "GROUP BY r.type")
    List<ReactionCountDTO> countReactionsByType(@Param("postId") Long postId);
}
