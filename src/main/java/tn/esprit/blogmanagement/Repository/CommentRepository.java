package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.blogmanagement.Entity.Comment;


public interface CommentRepository extends JpaRepository <Comment, Long> {
}
