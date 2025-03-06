package tn.esprit.blogmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.blogmanagement.Entity.Comment;
import tn.esprit.blogmanagement.Repository.CommentRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment registerComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment updateComment(Long id, Comment updatedComment) {
        return commentRepository.findById(id).map(comment -> {
            comment.setContent(updatedComment.getContent());
            comment.setUser(updatedComment.getUser()); // Ensure user is correctly set
            comment.setLastUpdatedAt(new Date());  // Update only the 'lastUpdatedAt' field
            comment.setIsEdited(true); // Mark the comment as edited
            return commentRepository.save(comment);
        }).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> getCommentsForPost(Long postId) {
        // Fetch comments by post ID
        return commentRepository.findByPostId(postId);
    }

    // Helper method to extract mentioned usernames from the comment
    public List<String> extractMentions(String content) {
        List<String> mentions = new ArrayList<>();
        Pattern pattern = Pattern.compile("@(\\w+)");  // Regex to find mentions starting with '@'
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            mentions.add(matcher.group(1));  // Extract username without '@'
        }

        return mentions;
    }

}