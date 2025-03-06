package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.blogmanagement.Entity.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository <Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
