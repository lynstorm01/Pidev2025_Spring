package tn.esprit.blogmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.blogmanagement.DTO.PostDTO;
import tn.esprit.blogmanagement.Entity.Post;
import tn.esprit.blogmanagement.Repository.PostRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final ReactionService reactionService;

    @Autowired
    public PostService(PostRepository postRepository , ReactionService reactionService) {
        this.postRepository = postRepository;
        this.reactionService = reactionService;
    }

    @Override
    public Post registerPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post updatePost(Long id, Post updatedPost) {
        return postRepository.findById(id).map(post -> {
            post.setCategory(updatedPost.getCategory());
            post.setContent(updatedPost.getContent());
            post.setTitle(updatedPost.getTitle());
            post.setUser(updatedPost.getUser()); // ensure password is encoded
            post.setLastUpdatedAt(new Date());  // Update only the 'updatedAt' field
            return postRepository.save(post);
        }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // In PostService.java
    private PostDTO mapToDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .lastUpdatedAt(post.getLastUpdatedAt())
                // Add other post fields you need
                .build();
    }

    public PostDTO getPostWithReactions(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Use your existing PostDTO mapping method (not ReactionService's mapToDTO)
        PostDTO dto = mapToDTO(post); // Assuming you have a mapToDTO method in PostService

        // Set reaction counts and current user reaction
        dto.setReactionCounts(reactionService.getReactionCounts(postId));
        return dto;
    }
}