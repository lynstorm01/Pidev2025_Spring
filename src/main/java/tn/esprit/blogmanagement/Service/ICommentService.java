package tn.esprit.blogmanagement.Service;

import tn.esprit.blogmanagement.Entity.Comment;

import java.util.List;
import java.util.Optional;

public interface ICommentService {
    Comment registerComment(Comment comment);
    Optional<Comment> getCommentById(Long id);
    List<Comment> getAllComments();
    Comment updateComment(Long id,Comment updatedComment);
    void deleteComment(Long id);
}

