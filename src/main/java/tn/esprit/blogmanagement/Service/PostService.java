package tn.esprit.blogmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.blogmanagement.Entity.Post;
import tn.esprit.blogmanagement.Repository.PostRepository;
import tn.esprit.blogmanagement.Service.IPostService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
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

}