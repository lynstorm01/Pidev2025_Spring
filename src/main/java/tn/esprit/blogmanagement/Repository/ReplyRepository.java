package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.blogmanagement.Entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long commentId);  // Find replies by comment ID

}
