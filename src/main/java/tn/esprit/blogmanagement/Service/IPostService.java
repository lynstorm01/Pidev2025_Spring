package tn.esprit.blogmanagement.Service;

import tn.esprit.blogmanagement.Entity.Post;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    Post registerPost(Post post);
    Optional<Post> getPostById(Long id);
    List<Post> getAllPosts();
    Post updatePost(Long id,Post updatedPost);
    void deletePost(Long id);
}
